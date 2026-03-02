import module java.net.http;

private final String START_TAG = "<a href=\"";
private final String END_TAG = "\">";

void main() throws Exception{
  var client = HttpClient.newHttpClient();
  var artifacts = downloadArtifacts(client);
  var artifact = selectArtifact(artifacts);
  var downloadedFile = download(client, artifact);
  var unpackedDir = unzip(downloadedFile);
  run(unpackedDir, artifact);
}

List<Artifact> downloadArtifacts(HttpClient client) throws Exception {
    var request = HttpRequest.newBuilder()
      .uri(URI.create("https://product.ivyteam.io/"))
      .build();
  var response = client.send(request, HttpResponse.BodyHandlers.ofString());
  var artifacts = parse(response.body());
  artifacts = artifacts.stream().filter(a -> a.name().contains("Windows")).toList();
  return artifacts;
}

Artifact selectArtifact(List<Artifact> artifacts) {
  var index = 1;
  for (var artifact : artifacts) {
    IO.println(index++ + " -> " + artifact.name());
  }
  var selection = Integer.parseInt(IO.readln("Select artifact to install and run: ")); 
  var artifact = artifacts.get(selection - 1);
  return artifact;
}

Path download(HttpClient client, Artifact artifact) throws Exception {
  var req = HttpRequest.newBuilder()
      .uri(artifact.uri())
      .build(); 
  var downloadTo = Path.of("C:\\XIVY\\dev\\");
  if (artifact.name().contains("Engine")) {
    downloadTo = downloadTo.resolve("Engine");
  } else {
    downloadTo = downloadTo.resolve("Designer");
  }
  Files.createDirectories(downloadTo);
  IO.println("Download to " + downloadTo + "...");
  downloadTo = downloadTo.resolve(artifact.name());
  var resp = client.send(req, HttpResponse.BodyHandlers.ofFile(downloadTo));
  return downloadTo;
}

List<Artifact> parse(String body) {
  var artifacts = new ArrayList<Artifact>();
  var end = 0;
  while (true) {
    var start = body.indexOf(START_TAG, end);
    if (start < 0) {
      return artifacts;
    }
    end = body.indexOf(END_TAG, start);
    if (end < 0)  {
      return artifacts;
    }
    var uri = URI.create(body.substring(start + START_TAG.length(), end));
    var name = uri.getPath();
    var last = name.lastIndexOf('/');
    name = name.substring (last + 1); 
    var artifact = new Artifact(name, uri);
    artifacts.add(artifact);
  }
}

void run(Path unpackedDir, Artifact artifact) throws IOException {
  var exe = unpackedDir;
  if (artifact.name().contains("Engine")) {
    exe = exe.resolve("bin").resolve("AxonIvyEngineC.exe");
  } else {
    exe = exe.resolve("AxonIvyDesigner.exe");
  }
  IO.println("Starting " + exe + "...");
  new ProcessBuilder(exe.toString())
      .inheritIO()
      .start();
}

Path unzip(Path zipFile) throws IOException {
  IO.println("Unpack " + zipFile + "...");  
  var targetDir = zipFile.getParent().resolve(zipFile.getFileName().toString().replace(".zip", ""));
  Files.createDirectories(targetDir);
  try (var zip = new ZipFile(zipFile.toFile())) {
    zip.stream().forEach(entry -> unzip(zip, entry, targetDir));
  }  
  return targetDir;
}

void unzip(ZipFile zip, ZipEntry entry, Path targetDir) {
  try {
    var target = targetDir.resolve(entry.getName());
    if (entry.isDirectory()) {
      Files.createDirectories(target);
    } else {
      Files.createDirectories(target.getParent());
      try (var in = zip.getInputStream(entry)) {
        Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
      }
    }
    Files.setLastModifiedTime(target, entry.getLastModifiedTime());
  } catch (IOException e) {
    throw new UncheckedIOException(e);
  }
}

record Artifact(String name, URI uri) {}
