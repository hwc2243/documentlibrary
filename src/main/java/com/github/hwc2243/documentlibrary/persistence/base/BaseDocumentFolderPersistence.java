package com.github.hwc2243.documentlibrary.persistence.base;




import com.github.hwc2243.documentlibrary.entity.DocumentFolderEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseDocumentFolderPersistence<E extends DocumentFolderEntity, ID> extends JpaRepository<E, ID>
{


    public E findFirstByNameAndLibraryIdAndParentFolderId(String name, Long libraryId, Long parentFolderId);


    public List<E> findByLibraryIdAndParentFolderId(Long libraryId, Long parentFolderId);

}
