package store.code.storage.way2;

import java.io.InputStream;
import java.util.Map;

public interface StorageProvider1 {

    Object put(String storageName, String identifier, Map<?, ?> metadata, InputStream inputStream);

    InputStream get(String storageName, String identifier);

    Map<String, Object> getMetadata(String storageName, String identifier);

    Object delete(String storageName, String identifier);

    // 增删改查 （根据 identifier 批量，可以循环解决， 文件上下级，由其实现类提供）
    // 这是一个通用的  存储提供者，其实现可以是本地文件存储，网络文件存储，对象存储等。

    // WebDAV
    /*
        1.Options、Head 和 Trace。
        主要由应用程序用来发现和跟踪服务器支持和网络行为。
        2.Get。
        检索文档。
        3.Put 和 Post。
        将文档提交到服务器。
        4.Delete。
        销毁资源或集合。
        5. Mkcol。
        创建集合。
        6.PropFind 和 PropPatch。
        针对资源和集合检索和设置属性。
        7.Copy 和 Move。
        管理命名空间上下文中的集合和资源。
        8. Lock 和 Unlock。
        改写保护。
    * */


//    ObjectResult putObject(StorageObject storageObject);
//
//    void deleteObject(ObjectModel objectModel);
//
//    DeleteObjectsResult deleteObjects(DeleteObjectsModel deleteObjectsModel);
//
//    boolean doesObjectExist(ObjectModel objectModel);
//
//    Map<String, Object> getMetadata(ObjectModel objectModel);
//
//    StorageObject getObject(ObjectModel objectModel);
//
//    ListObjectsResult listObjects(ListObjectsModel listObjectsModel);

    /*
    AppendObject
    CopyObject
    moveObject

    GetObjectMeta
    PutObjectMeta

    ----
    PutObjectResult putObject(PutObjectRequest putObjectRequest)

    void deleteObject(GenericRequest genericRequest)

    DeleteObjectsResult deleteObjects(DeleteObjectsRequest deleteObjectsRequest)

    boolean doesObjectExist(GenericRequest genericRequest)

    ObjectMetadata getObjectMetadata(GenericRequest genericRequest)

    SimplifiedObjectMeta getSimplifiedObjectMeta(GenericRequest genericRequest)

    OSSObject getObject(GetObjectRequest getObjectRequest)

    ObjectListing listObjects(ListObjectsRequest listObjectsRequest);

    CopyObjectResult copyObject(CopyObjectRequest copyObjectRequest)

    * */


}
