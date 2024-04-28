package store.code.demo.base;

import artoria.io.IOUtils;
import artoria.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarFileLoadDemo {
    private static final Logger log = LoggerFactory.getLogger(JarFileLoadDemo.class);

    /**
     * 查询 Classpath 下第一层的目录（不进行扩展了）.
     * @param clazz 类加载器需要使用的类
     * @param queryPath 要查询的路径（比如“elasticsearch/mapping/”）
     * @return 查询到的目录的集合
     */
    public List<String> findClasspathDirNames(Class<?> clazz, String queryPath) throws IOException {
        // 参数校验
        Assert.notBlank(queryPath, "queryPath 不能为空！");
        Assert.notNull(clazz, "clazz 不能为空！");
        if (!(queryPath.endsWith("/")||queryPath.endsWith("\\"))) { queryPath+="/"; }
        // 使用 ClassLoader 获取资源的 URL
        ClassLoader classLoader = clazz.getClassLoader();
        List<URL> urls = new ArrayList<URL>();
        Enumeration<URL> resources = classLoader.getResources(queryPath);
        while (resources.hasMoreElements()) { urls.add(resources.nextElement()); }
        Assert.isTrue(urls.size() == 1, "待查询的路径需要唯一！");
        URL targetUrl = CollectionUtils.getFirst(urls);
        // 判空，如果 targetUrl 是空，则结束流程，路径类似于这样：
        // “jar:file:/data/apps/ins-xhtask-platform/ins-xhtask-platform.jar!/BOOT-INF/classes!/elasticsearch/mapping/”
        log.debug("find the target url is {}. ", targetUrl);
        if (targetUrl == null) { return null; }
        // 加载资源
        if ("jar".equals(targetUrl.getProtocol())) {
            // >>资源路径处理
            String targetPath = targetUrl.getPath(); int indexOf = targetPath.indexOf("!");
            // 获取 JAR 文件路径，类似于：/data/apps/ins-xhtask-platform/ins-xhtask-platform.jar
            // 获取 JAR 中资源路径，类似于：BOOT-INF/classes!/elasticsearch/mapping/
            String jarPath = targetPath.substring(5, indexOf),  entryPathPrefix = targetPath.substring(indexOf + 2);
            // 但是在 JarEntry 搜索时，是没有感叹号的，即：BOOT-INF/classes/elasticsearch/mapping/
            entryPathPrefix = entryPathPrefix.replaceAll("!", "");
            log.info("The jar path = {}, entry path prefix = {}", jarPath, entryPathPrefix);
            // 使用JarFile来处理JAR文件
            JarFile jarFile = new JarFile(jarPath);
            try {
                List<String> resultList = new ArrayList<String>();
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement(); String entryName = entry.getName();
                    if (StringUtils.equals(entryName, entryPathPrefix)) { continue; }
                    // 判断指定前缀对应的 JarEntry，只要一层的目录
                    if (entryName.startsWith(entryPathPrefix) && entry.isDirectory()) {
                        resultList.add(entryName.replace(entryPathPrefix, ""));
                    }
                }
                return resultList;
            } finally { CloseUtils.closeQuietly(jarFile); }
        }
        else {
            File[] list = new File(targetUrl.getFile()).listFiles();
            List<String> resultList = new ArrayList<String>();
            if (ArrayUtils.isEmpty(list)) { return resultList; }
            for (File file : list) {
                if (file == null || file.isFile()) { continue; }
                resultList.add(file.getName());
            }
            return resultList;
        }
    }

    /**
     * 从 Classpath 下加载资源（jar 包情况和本地情况，都可以加载到）.
     * @param clazz 类加载器需要使用的类
     * @param queryPath 要查询的路径（比如“elasticsearch/mapping/”）
     * @param resName 要查询的资源名称（最新的：@latest@、最老的：@oldest@、指定的：“20240415.json”）
     * @return 加载到的资源的 byte 数组
     */
    public byte[] loadClasspathFile(Class<?> clazz, String queryPath, String resName) throws IOException {
        // 参数校验
        Assert.notBlank(queryPath, "queryPath 不能为空！");
        Assert.notBlank(resName, "resName 不能为空！");
        Assert.notNull(clazz, "clazz 不能为空！");
        // 最新的：@latest@、最老的：@oldest@、指定的：“20240415.json”
        final String latestTag = "@latest@", oldestTag = "@oldest@";
        if (!(queryPath.endsWith("/")||queryPath.endsWith("\\"))) { queryPath+="/"; }
        // 使用 ClassLoader 获取资源的 URL
        ClassLoader classLoader = clazz.getClassLoader();
        List<URL> urls = new ArrayList<URL>();
        Enumeration<URL> resources = classLoader.getResources(queryPath);
        while (resources.hasMoreElements()) { urls.add(resources.nextElement()); }
        Assert.isTrue(urls.size() == 1, "待查询的路径需要唯一！");
        URL targetUrl = CollectionUtils.getFirst(urls);
        // 判空，如果 targetUrl 是空，则结束流程，路径类似于这样：
        // “jar:file:/data/apps/ins-xhtask-platform/ins-xhtask-platform.jar!/BOOT-INF/classes!/elasticsearch/mapping/”
        log.debug("find the target url is {}. ", targetUrl);
        if (targetUrl == null) { return null; }
        // 加载资源
        if ("jar".equals(targetUrl.getProtocol())) {
            // >>资源路径处理
            String targetPath = targetUrl.getPath(); int indexOf = targetPath.indexOf("!");
            // 获取 JAR 文件路径，类似于：/data/apps/ins-xhtask-platform/ins-xhtask-platform.jar
            // 获取 JAR 中资源路径，类似于：BOOT-INF/classes!/elasticsearch/mapping/
            String jarPath = targetPath.substring(5, indexOf),  entryPathPrefix = targetPath.substring(indexOf + 2);
            // 但是在 JarEntry 搜索时，是没有感叹号的，即：BOOT-INF/classes/elasticsearch/mapping/
            entryPathPrefix = entryPathPrefix.replaceAll("!", "");
            log.info("The jar path = {}, entry path prefix = {}", jarPath, entryPathPrefix);
            // 使用JarFile来处理JAR文件
            TreeMap<String, JarEntry> map = new TreeMap<String, JarEntry>();
            JarFile jarFile = new JarFile(jarPath);
            try {
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    // 判断指定前缀对应的 JarEntry
                    if (entry.getName().startsWith(entryPathPrefix) && !entry.isDirectory()) {
                        map.put(entry.getName(), entry);
                    }
                }
                if (MapUtils.isEmpty(map)) { return null; }
                // 处理指定的要查询的资源文件的情况
                if (!latestTag.equals(resName) && !oldestTag.equals(resName)) {
                    return IOUtils.toByteArray(jarFile.getInputStream(map.get(resName)));
                }
                // 处理最新的或者最老的资源的情况
                boolean first = true; JarEntry latestEntry = null, oldestEntry = null;
                for (Map.Entry<String, JarEntry> entry : map.entrySet()) {
                    log.debug("The entry path = {}", entry.getKey());
                    if (first) { oldestEntry = entry.getValue(); first = false; }
                    latestEntry = entry.getValue();
                }
                if (latestTag.equals(resName)) {
                    return IOUtils.toByteArray(jarFile.getInputStream(latestEntry));
                }
                else { return IOUtils.toByteArray(jarFile.getInputStream(oldestEntry)); }
            } finally { CloseUtils.closeQuietly(jarFile); }
        }
        else {
            String[] list = new File(targetUrl.getFile()).list();
            log.debug("find the target resource list is {}. ", Arrays.toString(list));
            if (ArrayUtils.isEmpty(list)) { return null; }
            // 加载最新的资源文件（数组的最后一个就是最新的，按照规范来的话）
            if (latestTag.equals(resName)) {
                return IOUtils.toByteArray(classLoader.getResourceAsStream(queryPath + list[list.length - 1]));
            }
            else if (oldestTag.equals(resName)) {
                return IOUtils.toByteArray(classLoader.getResourceAsStream(queryPath + list[0]));
            }
            else { return IOUtils.toByteArray(classLoader.getResourceAsStream(queryPath + resName)); }
        }
    }

}
