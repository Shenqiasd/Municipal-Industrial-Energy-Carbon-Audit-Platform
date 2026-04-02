package com.energy.audit.service.setting.impl;

import com.energy.audit.common.exception.BusinessException;
import com.energy.audit.common.util.SecurityUtils;
import com.energy.audit.dao.mapper.setting.BsProductMapper;
import com.energy.audit.model.entity.setting.BsProduct;
import com.energy.audit.service.setting.ProductSettingService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Product setting service implementation
 */
@Service
public class ProductSettingServiceImpl implements ProductSettingService {

    private final BsProductMapper productMapper;

    public ProductSettingServiceImpl(BsProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    @Override
    public BsProduct getById(Long id) {
        BsProduct product = productMapper.selectById(id);
        if (product == null) {
            throw new BusinessException("Product not found: " + id);
        }
        return product;
    }

    @Override
    public List<BsProduct> list(BsProduct query) {
        if (query.getEnterpriseId() == null) {
            query.setEnterpriseId(SecurityUtils.getCurrentEnterpriseId());
        }
        return productMapper.selectList(query);
    }

    @Override
    public void create(BsProduct product) {
        String operator = SecurityUtils.getCurrentUsername();
        Long enterpriseId = SecurityUtils.getCurrentEnterpriseId();
        product.setCreateBy(operator);
        product.setUpdateBy(operator);
        if (product.getEnterpriseId() == null) {
            product.setEnterpriseId(enterpriseId);
        }
        productMapper.insert(product);
    }

    @Override
    public void update(BsProduct product) {
        getById(product.getId());
        product.setUpdateBy(SecurityUtils.getCurrentUsername());
        productMapper.updateById(product);
    }

    @Override
    public void delete(Long id) {
        getById(id);
        productMapper.deleteById(id, SecurityUtils.getCurrentUsername());
    }
}
