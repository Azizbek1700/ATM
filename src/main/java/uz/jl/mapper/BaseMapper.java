package uz.jl.mapper;

/**
 * @author Elmurodov Javohir, Mon 6:15 PM. 12/6/2021
 */

import uz.jl.dtos.BaseDto;

/**
 * @param <E>  -> Entity
 * @param <D>  -> Dto
 */
public abstract class BaseMapper<E, D extends BaseDto> {
    abstract E fromDto(D d);

    abstract D toDto(E e);
}
