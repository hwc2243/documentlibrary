package com.github.hwc2243.documentlibrary.persistence.base;




import com.github.hwc2243.documentlibrary.entity.DocumentFileEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseDocumentFilePersistence<E extends DocumentFileEntity, ID> extends JpaRepository<E, ID>
{

}