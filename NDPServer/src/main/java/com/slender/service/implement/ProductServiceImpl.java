package com.slender.service.implement;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.slender.annotation.RemoveCache;
import com.slender.annotation.ServiceCache;
import com.slender.config.manager.FileManager;
import com.slender.dto.product.ProductAddRequest;
import com.slender.dto.product.ProductUpdateRequest;
import com.slender.dto.product.ProductPageRequest;
import com.slender.entity.Product;
import com.slender.enumeration.FileType;
import com.slender.exception.file.ExcelExportException;
import com.slender.exception.product.ProductNotFoundException;
import com.slender.exception.user.MerchantNotFoundException;
import com.slender.mapper.ProductMapper;
import com.slender.repository.MerchantRepository;
import com.slender.repository.ProductRepository;
import com.slender.service.interfase.ProductService;
import com.slender.vo.FileData;
import com.slender.vo.PageData;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
    private final ProductRepository productRepository;
    private final MerchantRepository merchantRepository;
    private final FileManager fileManager;

    @Override
    @ServiceCache
    public Product get(Long pid) {
        return productRepository.findById(pid)
                .orElseThrow(ProductNotFoundException::new);
    }

    @Override
    public PageData<Product> get(ProductPageRequest request) {
        return productRepository.get(request);
    }

    @Override
    public void add(Long uid, ProductAddRequest request) {
        merchantRepository.getByUid(uid)
                .ifPresentOrElse(merchant -> this.save(new Product(merchant.getMid(), request)),
                        () -> {throw new MerchantNotFoundException();});
    }

    @Override
    @RemoveCache
    public void update(Long pid, ProductUpdateRequest request) {
        productRepository.update(pid,request);
    }

    @Override
    @RemoveCache
    public void delete(Long pid) {
        this.removeById(pid);
    }

    @Override
    public FileData updateImage(Long pid, MultipartFile file){
        this.get(pid);
        try {
            String url = fileManager.upload(file.getOriginalFilename(), file.getBytes(), FileType.PRODUCT);
            return new FileData(url);
        } catch (IOException e) {
            log.error("获取文件内容失败",e);
        }
        return null;
    }

    @Override
    public void export(HttpServletResponse response) {
        List<String> column = List.of("商品ID", "商家ID", "分类ID", "创建时间", "更新时间",
                "商品名称", "库存", "销量", "价格", "图片", "描述", "产品状态", "删除状态");
        List<Product> data = this.list();
        try(
            XSSFWorkbook excel=new XSSFWorkbook();
            ServletOutputStream out=response.getOutputStream()
        ){
            XSSFSheet sheet = excel.createSheet("products");
            XSSFRow firstRow = sheet.createRow(0);
            int columnSize = column.size();
            for (int i = 0; i < columnSize; i++)
                firstRow.createCell(i).setCellValue(column.get(i));
            int len = data.size();
            for (int i = 0; i < len; i++) {
                XSSFRow row = sheet.createRow(i + 1);
                Product product = data.get(i);
                row.createCell(0).setCellValue(product.getPid());
                row.createCell(1).setCellValue(product.getMid());
                row.createCell(2).setCellValue(product.getCid());
                row.createCell(3).setCellValue(product.getCreateTime());
                row.createCell(4).setCellValue(product.getUpdateTime());
                row.createCell(5).setCellValue(product.getProductName());
                row.createCell(6).setCellValue(product.getRemain());
                row.createCell(7).setCellValue(product.getSaleAmount());
                row.createCell(8).setCellValue(product.getPrice().doubleValue());
                row.createCell(9).setCellValue(product.getImage()!=null ? product.getImage() : "null");
                row.createCell(10).setCellValue(product.getDescription()!=null ? product.getDescription() : "null");
                row.createCell(11).setCellValue(product.getStatus().getValue());
                row.createCell(12).setCellValue(product.getDeleted().getValue());
            }
            response.setHeader("Content-Disposition", "attachment; filename=products.xlsx");
            excel.write(out);
        }catch (IOException _){
            throw new ExcelExportException();
        }
    }
}
