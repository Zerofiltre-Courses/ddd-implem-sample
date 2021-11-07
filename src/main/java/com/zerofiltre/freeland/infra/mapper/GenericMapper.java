package com.zerofiltre.freeland.infra.mapper;

public interface GenericMapper<T, Z> {

  public T toDTO(Z entity);

  public Z toEntity(T dto);

}
