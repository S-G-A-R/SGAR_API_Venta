package com.sgar.SGARventaAPI.mapper;

import com.sgar.SGARventaAPI.dto.CategoriaProducto.CategoriaProductoRequets;
import com.sgar.SGARventaAPI.dto.CategoriaProducto.CategoriaProductoResponse;
import com.sgar.SGARventaAPI.modelos.CategoriaProducto;
import org.springframework.stereotype.Component;

@Component
public class CategoriaProductoMapper {

    public CategoriaProductoResponse toDTO(CategoriaProducto categoriaProducto) {
        if (categoriaProducto == null) {
            return null;
        }
        
        CategoriaProductoResponse dto = new CategoriaProductoResponse();
        dto.setId(categoriaProducto.getId());
        dto.setNombreCat(categoriaProducto.getNombreCat());
        dto.setAsociadoId(categoriaProducto.getAsociadoId());
        
        return dto;
    }

    public CategoriaProducto toEntity(CategoriaProductoResponse dto) {
        if (dto == null) {
            return null;
        }
        
        CategoriaProducto categoriaProducto = new CategoriaProducto();
        categoriaProducto.setId(dto.getId());
        categoriaProducto.setNombreCat(dto.getNombreCat());
        categoriaProducto.setAsociadoId(dto.getAsociadoId());
        
        return categoriaProducto;
    }

    public CategoriaProducto toEntity(CategoriaProductoRequets dto) {
        if (dto == null) {
            return null;
        }
        
        CategoriaProducto categoriaProducto = new CategoriaProducto();
        categoriaProducto.setNombreCat(dto.getNombreCat());
        categoriaProducto.setAsociadoId(dto.getAsociadoId());
        
        return categoriaProducto;
    }

    public void updateEntityFromDTO(CategoriaProducto categoriaProducto, CategoriaProductoRequets dto) {
        if (dto == null || categoriaProducto == null) {
            return;
        }
        
        categoriaProducto.setNombreCat(dto.getNombreCat());
        categoriaProducto.setAsociadoId(dto.getAsociadoId());
    }
}
