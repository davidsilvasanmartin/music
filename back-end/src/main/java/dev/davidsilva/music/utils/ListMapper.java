package dev.davidsilva.music.utils;

import java.util.List;

public interface ListMapper<From, To> {
    List<To> map(List<From> from);
}
