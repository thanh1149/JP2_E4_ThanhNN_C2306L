package src.Generic;

import java.util.Optional;

public interface IBankService<T> {
    Optional<T> findByID(int id);
}
