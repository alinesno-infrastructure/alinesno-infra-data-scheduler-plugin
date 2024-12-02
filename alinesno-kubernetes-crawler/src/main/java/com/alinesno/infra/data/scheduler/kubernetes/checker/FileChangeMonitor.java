package com.alinesno.infra.data.scheduler.kubernetes.checker;

import java.io.IOException;
import java.nio.file.*;

public class FileChangeMonitor {

    private final Path dir;

    public FileChangeMonitor(Path dir) {
        this.dir = dir;
    }

    public void start() throws IOException, InterruptedException {
        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            // 注册监听服务到指定目录
            this.dir.register(watchService,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_MODIFY);

            while (true) {
                WatchKey key;
                try {
                    key = watchService.take();  // 阻塞等待事件
                } catch (InterruptedException ex) {
                    return;
                }

                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    if (kind == StandardWatchEventKinds.OVERFLOW) {
                        continue;  // 溢出事件，通常忽略
                    }

                    // 获取受影响的文件名
                    @SuppressWarnings("unchecked")
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path filename = ev.context();
                    Path child = dir.resolve(filename);

                    System.out.format("%s: %s\n", event.kind().name(), child);
                    // 在这里添加你的逻辑，例如记录日志
                    logFileChange(event, child);
                }

                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }
        }
    }

    private void logFileChange(WatchEvent<?> event, Path file) {
        // 这里可以添加日志记录逻辑
        // 例如，将变更信息写入日志文件
        System.out.println("File " + file + " has been " + event.kind().name());
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Path dir = Paths.get("/path/to/your/website");  // 监听的目录
        new FileChangeMonitor(dir).start();
    }
}