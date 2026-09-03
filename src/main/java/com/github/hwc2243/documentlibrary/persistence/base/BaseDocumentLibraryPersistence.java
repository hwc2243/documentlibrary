package com.github.hwc2243.documentlibrary.persistence.base;




import com.github.hwc2243.documentlibrary.entity.DocumentLibraryEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseDocumentLibraryPersistence<E extends DocumentLibraryEntity, ID> extends JpaRepository<E, ID>
{

}