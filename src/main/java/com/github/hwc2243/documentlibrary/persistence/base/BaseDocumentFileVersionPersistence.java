package com.github.hwc2243.documentlibrary.persistence.base;




import com.github.hwc2243.documentlibrary.entity.DocumentFileVersionEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseDocumentFileVersionPersistence<E extends DocumentFileVersionEntity, ID> extends JpaRepository<E, ID>
{

}
