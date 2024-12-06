package org.base.mapper;

import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;

@MapperConfig(componentModel = "jakarta", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface QuarkusMappingConfig {

}
