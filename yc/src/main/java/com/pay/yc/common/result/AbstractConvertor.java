package com.pay.yc.common.result;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.pay.yc.common.result.dto.ListResultDTO;
import com.pay.yc.common.result.dto.PageResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.pay.yc.common.result.dto.ResultDTO;

/**
 * 
 * @author dumingxin
 *
 * @param <Model>
 * @param <DTO>
 */
public abstract class AbstractConvertor <Model, DTO>{
	@Autowired(required = false)
	protected CurrentUserFactoryBean currentUserFactoryBean;

	public abstract Model toModel(DTO dto, Model model);
	public Model toModel(DTO dto){

		Type[] types =((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments();
		Class<Model> clazz = (Class)types[1];
		try {
			return this.toModel(dto, clazz.newInstance());
		} catch (Exception e) {
			return this.toModel(dto, null);
		}
	}
	public DTO toDTO(Model model) {
		return this.toDTO(model, false);
	}

	public abstract DTO toDTO(Model model, boolean arg1);

	public final List<Model> toListModel(List<DTO> dtoList, Function<DTO, Model> toModelMapper) {
		List modelList = (List) dtoList.stream().map(toModelMapper).collect(Collectors.toList());
		return modelList;
	}

	public final List<Model> toListModel(List<DTO> dtoList) {
		List modelList = (List) dtoList.stream().map((dto) -> {
			return this.toModel(dto);
		}).collect(Collectors.toList());
		return modelList;
	}

	public List<DTO> toListDTO(List<Model> modelList, Function<Model, DTO> toDTOMapper) {
		return (List) modelList.stream().map(toDTOMapper).collect(Collectors.toList());
	}

	public final List<DTO> toListDTO(List<Model> modelList) {
		List dtoList = (List) modelList.stream().map((model) -> {
			return this.toDTO(model, true);
		}).collect(Collectors.toList());
		return dtoList;
	}

	public final Page<DTO> toPageDTO(Page<Model> modelPage, Function<Model, DTO> toDTOMapper) {
		List modelList = modelPage.getContent();
		List dtoList = this.toListDTO(modelList, toDTOMapper);
		long totalElements = modelPage.getTotalElements();
		PageImpl dtoPage = new PageImpl(dtoList, this.getPageable(modelPage), totalElements);
		return dtoPage;
	}

	public final Page<DTO> toPageDTO(Page<Model> modelPage) {
		List modelList = modelPage.getContent();
		List dtoList = this.toListDTO(modelList);
		long totalElements = modelPage.getTotalElements();
		PageImpl dtoPage = new PageImpl(dtoList, this.getPageable(modelPage), totalElements);
		return dtoPage;
	}

	public final ResultDTO<DTO> toResultDTO(Model model, Function<Model, DTO> toDTOMapper) {
		Object dto = toDTOMapper.apply(model);
		ResultDTO resultDTO = ResultDTO.success(dto);
		return resultDTO;
	}

	public final ResultDTO<DTO> toResultDTO(Model model) {
		Object dto = model == null ? null : this.toDTO(model);
		ResultDTO resultDTO = ResultDTO.success(dto);
		return resultDTO;
	}

	public final ListResultDTO<DTO> toResultDTO(List<Model> modelList, Function<Model, DTO> toDTOMapper) {
		List dtoList = this.toListDTO(modelList, toDTOMapper);
		ListResultDTO resultDTO = ListResultDTO.success(dtoList);
		return resultDTO;
	}

	public final ListResultDTO<DTO> toResultDTO(List<Model> modelList) {
		List dtoList = this.toListDTO(modelList);
		ListResultDTO resultDTO = ListResultDTO.success(dtoList);
		return resultDTO;
	}

	public final PageResultDTO<DTO> toResultDTO(Page<Model> modelPage, Function<Model, DTO> toDTOMapper) {
		List dtoList = this.toListDTO(modelPage.getContent(), toDTOMapper);
		PageResultDTO resultDTO = PageResultDTO.success(dtoList, modelPage);
		return resultDTO;
	}

	public final PageResultDTO<DTO> toResultDTO(Page<Model> modelPage) {
		List dtoList = this.toListDTO(modelPage.getContent());
		PageResultDTO resultDTO = PageResultDTO.success(dtoList, modelPage);
		return resultDTO;
	}

//	protected final void loadAuditToDTO(Auditable model, AbstractAuditDTO dto) {
//		dto.setCreatedBy(model.getCreatedBy());
//		dto.setLastModifiedBy(model.getLastModifiedBy());
//		dto.setCreatedDate(model.getCreatedDate());
//		dto.setLastModifiedDate(model.getLastModifiedDate());
//	}

	protected Pageable getPageable(Page<Model> modelPage) {
		try {
			Field pageableField = PageImpl.class.getSuperclass().getDeclaredField("pageable");
			pageableField.setAccessible(true);
			return (Pageable) pageableField.get(modelPage);
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException arg2) {
			return null;
		}
	}

	protected Object getCurrentUser() {
		return this.currentUserFactoryBean == null ? null : this.currentUserFactoryBean.getCurrentUser();
	}
}
