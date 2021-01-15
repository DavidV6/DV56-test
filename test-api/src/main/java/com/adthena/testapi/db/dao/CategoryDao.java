package com.adthena.testapi.db.dao;

import com.adthena.testapi.db.entities.CategoryEntity;
import java.util.List;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface CategoryDao {

  @SqlQuery("SELECT * FROM category where catid = :catid")
  @RegisterConstructorMapper(CategoryEntity.class)
  CategoryEntity getCategoryById(@Bind("catid") Integer catid);

  /**
   * Create or update depending on the presence of an id and whether it's higher than 0 or not.
   * @param category entity to create or update
   * @return new entity
   */
  default CategoryEntity upsert(CategoryEntity category) {
    if (category.getCatid() != null && category.getCatid() > 0) {
      update(category.getCatid(), category.getCatgroup(), category.getCatname(),
          category.getCatdesc());
      return category;
    }
    final CategoryEntity entity = category.withCatid(nextId());
    create(entity.getCatid(), entity.getCatgroup(), entity.getCatname(), entity.getCatdesc());
    return entity;
  }

  @SqlQuery("SELECT MAX(catid)+1 FROM category")
  Integer nextId();

  @SqlUpdate("INSERT INTO category (catid, catgroup, catname, catdesc) VALUES (:catid, :catgroup, :catname, :catdesc)")
  Integer create(
      @Bind("catid") Integer catid,
      @Bind("catgroup") String catgroup,
      @Bind("catname") String catname,
      @Bind("catdesc") String catdesc
  );

  @SqlUpdate("UPDATE category SET catgroup=:catgroup, catname=:catname, catdesc=:catdesc WHERE catid=:catid")
  Integer update(
      @Bind("catid") Integer catid,
      @Bind("catgroup") String catgroup,
      @Bind("catname") String catname,
      @Bind("catdesc") String catdesc
  );

  @SqlQuery("SELECT * FROM category ORDER BY catid ASC")
  @RegisterConstructorMapper(CategoryEntity.class)
  List<CategoryEntity> list();
}
