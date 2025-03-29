# Repositories in a Spring project

## Differences between `PagingAndSortinRepository`, `JpaRepository` and `JpaSpecificationExecutor`

Below is an explanation of the differences between these three repository interfaces and some guidance on their correct
usage:

1. **PagingAndSortingRepository**
    - **Purpose**: Extends the basic CRUD (Create, Read, Update, Delete) operations with methods that assist with
      pagination and sorting results.
    - **Use Case**: When you need simple pagination and sorting capabilities without extra JPA-specific operations.
    - **Example Methods**:
        - findAll(Pageable pageable)
        - findAll(Sort sort)

2. **JpaRepository**
    - **Purpose**: Extends PagingAndSortingRepository and, in turn, CrudRepository. It adds JPA-specific methods such as
      flushing the persistence context and batch deletion operations.
    - **Use Case**: When you need a richer set of CRUD operations along with paging and sorting. This is typically the
      most commonly used interface for data repositories in Spring Data JPA.
    - **Example Methods**:
        - flush()
        - saveAndFlush(entity)
        - deleteInBatch(Iterable entities)

3. **JpaSpecificationExecutor**
    - **Purpose**: Provides support for executing JPA Criteria queries using the Specification API. It does not extend
      Repository itself but acts as a complement to the repository interface.
    - **Use Case**: When you need to build dynamic queries (for example, filtering based on arbitrary conditions
      provided at runtime) using the JPA Criteria API.
    - **Example Methods**:
        - findOne(Specification spec)
        - findAll(Specification spec)
        - count(Specification spec)

### How to Use Them Correctly

- **Basic CRUD and Paging/Sorting**:
  Extend your repository interface with `JpaRepository` if you need most of the standard operations. For example:

``` java
  package com.example.repository;

  import com.example.domain.Album;
  import org.springframework.data.jpa.repository.JpaRepository;

  public interface AlbumRepository extends JpaRepository<Album, Long> {
      // Additional query methods can be declared here
  }
```

This provides CRUD methods along with methods to perform paging and sorting.

- **Dynamic Queries Using Specifications**:
  If your application requires building dynamic queries (for example, search filters that vary at runtime), extend your
  repository interface with `JpaSpecificationExecutor` in addition to `JpaRepository`:

``` java
  package com.example.repository;

  import com.example.domain.Album;
  import org.springframework.data.jpa.repository.JpaRepository;
  import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

  public interface AlbumRepository extends JpaRepository<Album, Long>, JpaSpecificationExecutor<Album> {
      // No additional methods are required; you can build specifications for dynamic queries.
  }
```

You would then create Specification implementations:

``` java
  package com.example.specification;

  import com.example.domain.Album;
  import org.springframework.data.jpa.domain.Specification;

  public class AlbumSpecification {
      public static Specification<Album> hasTitle(String title) {
          return (root, query, criteriaBuilder) ->
              criteriaBuilder.equal(root.get("title"), title);
      }
  }
```

And query your repository like this:

``` java
  List<Album> albumsWithTitle = albumRepository.findAll(AlbumSpecification.hasTitle("Some Title"));
```

- **When to Choose PagingAndSortingRepository**:
  If you don’t need the additional JPA-specific functionalities offered by JpaRepository (like batch operations or
  flushing), and your operations are simple, you can extend `PagingAndSortingRepository`. However, in most cases, using
  `JpaRepository` is preferred because it incorporates all its functionality plus more.
