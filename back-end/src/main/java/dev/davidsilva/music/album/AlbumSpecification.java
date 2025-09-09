package dev.davidsilva.music.album;

import dev.davidsilva.music.search.AbstractSpecification;
import dev.davidsilva.music.search.SearchCriteria;
import jakarta.persistence.criteria.*;

import java.util.List;


public class AlbumSpecification extends AbstractSpecification<Album> {
    public AlbumSpecification(List<SearchCriteria> criteria) {
        super(criteria);
    }

    public Predicate toPredicate(
            Root<Album> root, CriteriaQuery<?> query,
            CriteriaBuilder builder
    ) {
        assert query != null;

        /*
            Setting distinct true is needed for performing searches such as "genres.name:contains:House". Suppose that one album
            has the 2 genres "House" and "Progressive House". If we don't add "distinct", this album would appear twice on the results.
         */
        query.distinct(true);

        /*
            FOR SELECT QUERIES:

            We need to manually join the tables here. This is because performing sorting and searching by different sub-keys
            is causing problems. For example, when searching by "genres.name:contains:House", the default sort is by artist name.
            A query such as the following one would be generated:

            select
                distinct a1_0.id,
                         a1_0.album,
                         a1_0.art_path,
                         a1_0.artist_id,
                         a1_0.beets_id,
                         a1_0.created_at,
                         a1_0.mb_album_id,
                         a1_0.updated_at,
                         a1_0.original_year
            from
                albums a1_0
                    left join
                artists a2_0
                on a2_0.id=a1_0.artist_id
                    join
                albums_genres g1_0
                on a1_0.id=g1_0.album_id
                    join
                genres g1_1
                on g1_1.id=g1_0.genre_id
            where
                lower(cast(g1_1.name as text)) like 'house' escape ''
            order by
                a2_0.name

            But this query is invalid. The a2_0.name property, which we are trying to sort by, is not included in the SELECT,
            so it's impossible to sort by it. Hence, we need to add joins so all properties are included in the SELECT.

            A similar issue happens when searching by artist.name.

            The current solution of eagerly fetching dependencies with "fetch" seems to work well and does not duplicate
            Albums. Using "fetch" fixes the following kind of errors:

            ```
            JDBC exception executing SQL [select distinct ...] [ERROR: for SELECT DISTINCT, ORDER BY expressions must
            appear in select list\n  Position: 296] [n/a]; SQL [n/a]
            ```
         */

        /*
            FOR COUNT QUERIES:

            This function is used to generate a count query before the actual select. In count queries, the root entity
            (Album in this case) is not returned, that is: it's not included in the SELECT list. Hibernate does not like
            it when you use fetch() and then do not request the parent entity (the entity that owns the relationships,
            Album) in the results.

            So, for count and other queries that don't require the Album, we use join() instead of fetch(). This change
            fixes the following error:

            ```
            org.hibernate.query.SemanticException: Query specified join fetching, but the owner of the fetched
            association was not present in the select list
            [SqmSingularJoin(dev.davidsilva.music.album.Album(409814089722333).artist(409814089740416) : artist)]
            ```
         */

        if (query.getResultType() != null && query.getResultType().equals(Album.class)) {
            // For select queries, use fetch joins for performance and to fix the bug explained above
            root.fetch("artist", JoinType.LEFT);
            root.fetch("genres", JoinType.LEFT);

            // TODO REVIEW !
            // For sorting by songs.title, we need to include songs in the query
            // But we need to use a regular join (not fetch) to avoid duplication when an album has multiple genres
            // This allows sorting by song properties while preventing song duplication
            if (query.getOrderList() != null && !query.getOrderList().isEmpty()) {
                boolean sortBySongProperty = query.getOrderList().stream()
                        .anyMatch(order -> order.getExpression().toString().startsWith("songs."));

                if (sortBySongProperty) {
                    // Use a regular join for songs when sorting by song properties
                    root.join("songs", JoinType.LEFT);
                }
            }
        } else {
            // For count queries or projections, use regular joins
            root.join("artist", JoinType.LEFT);
            root.join("genres", JoinType.LEFT);
            // Don't join songs here to maintain consistency
        }

        return super.toPredicate(root, query, builder);
    }
}
