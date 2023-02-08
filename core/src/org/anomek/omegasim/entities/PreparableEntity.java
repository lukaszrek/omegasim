package org.anomek.omegasim.entities;

import org.anomek.omegasim.OmegaSim;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public interface PreparableEntity {
    List<PreparedEntity> prepare();

    interface PreparedEntity {

        int layer();

        void update(long now);

        void render(OmegaSim sim);
    }

    static List<PreparedEntity> prepareAll(List<? extends PreparableEntity> entities) {
        return prepareAll(entities.stream());
    }

    static List<PreparedEntity> prepareAll(PreparableEntity... entities) {
        return prepareAll(Stream.of(entities));
    }

    static List<PreparedEntity> prepareAll(Stream<? extends PreparableEntity> entities) {
        return entities
                .filter(Objects::nonNull)
                .flatMap(e -> e.prepare().stream())
                .collect(toList());
    }

}
