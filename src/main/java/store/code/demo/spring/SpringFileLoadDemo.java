package store.code.demo.spring;

import artoria.io.IOUtils;
import artoria.util.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpringFileLoadDemo {
    private static final Logger log = LoggerFactory.getLogger(SpringFileLoadDemo.class);

    /**
     * （基于 Spring）查询 Classpath 下指定路径下的第一层的目录（不进行扩展了）.
     * @param pathPattern 路径的 pattern（比如：classpath*:your-directory/*）
     * @return 查询到的目录名称或者 Null
     * @deprecated 经过测试，在本地可以查询到目录名称，一旦到 jar 中时，查不到目录名称
     */
    public List<String> findClasspathDirNames(String pathPattern) throws IOException {
        // 构建 spring 的 PathMatchingResourcePatternResolver，并进行查询
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources(pathPattern);
        if (ArrayUtils.isEmpty(resources)) { return null; }
        // 获取资源的名称，注意此处的 resource.isFile 不管是文件夹还是文件都是 true
        List<String> resultList = new ArrayList<String>();
        for (org.springframework.core.io.Resource resource : resources) {
            if (resource == null) { continue; }
            resultList.add(resource.getFilename());
        }
        return resultList;
    }

    /**
     * （基于 Spring）从 Classpath 下加载指定资源（jar 包情况和本地情况，都可以加载到）.
     * @param filePath 要加载的资源的路径
     * @return 资源的字符内容
     */
    public String loadClasspathFile(String filePath) throws IOException {
        try {
            ClassPathResource resource = new ClassPathResource(filePath);
            return IOUtils.toString(resource.getInputStream(), "UTF-8");
        }
        catch (FileNotFoundException e) {
            log.debug("load classpath file, file not found error! ", e);
            return null;
        }
    }

}
