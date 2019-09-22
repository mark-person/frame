package com.ppx.cloud.example.tool.faq;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ppx.cloud.base.jdbc.MyCriteria;
import com.ppx.cloud.base.jdbc.MyDaoSupport;
import com.ppx.cloud.base.jdbc.page.Page;
import com.ppx.cloud.base.mvc.ControllerReturn;
import com.ppx.cloud.base.util.ApplicationUtils;

@Service
public class FaqImpl extends MyDaoSupport {
	
	
	public String getDirection(String tableName, String code) {
		
		
		return "";
	}

	@Transactional
	public List<Faq> list(Page page, Faq pojo) {
		
	    page.addDefaultOrderName("t.faq_id");
		
		MyCriteria c = createCriteria("where")
			.addAnd("t.faq_title like ?", "%", pojo.getFaqTitle(), "%");
		
		StringBuilder cSql = new StringBuilder("select count(*) from core_faq t").append(c);
		StringBuilder qSql = new StringBuilder("select * from core_faq t").append(c);
		
		List<Faq> list = queryForPage(Faq.class, page, cSql, qSql, c.getParaList());
		return list;
	}
	
	@Transactional
	public Map<String, Object> insert(Faq pojo) {
	   
		insertEntity(pojo);
		
		pojo.setFaqId(super.getLastInsertId());
		
    	String itemSql = "insert into core_faq_descr_item(faq_id, faq_descr_index, item_content) values(?, ?, ?)";
    	for (int i = 0; i < pojo.getSub().size(); i++) {
			getJdbcTemplate().update(itemSql, pojo.getFaqId(), i, pojo.getSub().get(i));
		}
    	
    	String itemAnswerSql = "insert into core_faq_answer_item(faq_id, faq_answer_index, item_content) values(?, ?, ?)";
    	for (int i = 0; i < pojo.getAnswerSub().size(); i++) {
			getJdbcTemplate().update(itemAnswerSql, pojo.getFaqId(), i, pojo.getAnswerSub().get(i));
		}
		
        return ControllerReturn.of("faqId", pojo.getFaqId());
    }
	
	public Faq get(Integer id) {
		String sql = "select * from core_faq where faq_id = ?";
		Faq pojo = getJdbcTemplate().queryForObject(sql, BeanPropertyRowMapper.newInstance(Faq.class), id);  
		
		String itemSql = "select item_content from core_faq_descr_item where faq_id = ? order by faq_descr_index";
		List<String> subList = getJdbcTemplate().queryForList(itemSql, String.class, id);
		pojo.setSub(subList);
		
		String itemAnswerSql = "select item_content from core_faq_answer_item where faq_id = ? order by faq_answer_index";
		List<String> answerSubList = getJdbcTemplate().queryForList(itemAnswerSql, String.class, id);
		pojo.setAnswerSub(answerSubList);
		
        return pojo;
    }
    
	@Transactional
    public Map<String, Object> update(Faq pojo) {
    	updateEntity(pojo);
    	
    	String delSql = "delete from core_faq_descr_item where faq_id = ?";
    	String itemSql = "insert into core_faq_descr_item(faq_id, faq_descr_index, item_content) values(?, ?, ?)";
    	getJdbcTemplate().update(delSql, pojo.getFaqId());
    	for (int i = 0; i < pojo.getSub().size(); i++) {
			getJdbcTemplate().update(itemSql, pojo.getFaqId(), i, pojo.getSub().get(i));
		}
    	
    	String delAnswerSql = "delete from core_faq_answer_item where faq_id = ?";
    	String itemAnswerSql = "insert into core_faq_answer_item(faq_id, faq_answer_index, item_content) values(?, ?, ?)";
    	getJdbcTemplate().update(delAnswerSql, pojo.getFaqId());
    	for (int i = 0; i < pojo.getAnswerSub().size(); i++) {
			getJdbcTemplate().update(itemAnswerSql, pojo.getFaqId(), i, pojo.getAnswerSub().get(i));
		}
    	
        return ControllerReturn.of();
    }
    
    public Map<String, Object> delete(Integer id) {
        return ControllerReturn.of();
    }

    
    public Map<String, Object> writeToLocal(Integer id, Boolean force) {
        
        String VERSION = "v1";
        
        Faq faq = get(id);
        
        Path mdPath = Paths.get("E:\\eclipse-workspace-2019-06-R\\frame\\doc\\faq-" + VERSION);
        Path imgPath = Paths.get(mdPath.toString() + "/" + "img");
        if (!Files.exists(mdPath)) {
            return ControllerReturn.of(20000, "文件夹不存在:" + mdPath);
        }
        if (!Files.exists(imgPath)) {
            return ControllerReturn.of(30000, "图片文件夹不存在:" + imgPath);
        }
        
        String srcImgPath = ApplicationUtils.JAR_PARENT_HOME + "img/faq/" + VERSION;
        
        
        Path mdFilePath = Paths.get(mdPath.toString() + "/" + faq.getFaqTitle() + ".md");
        if (!force && Files.exists(mdFilePath)) {
            return ControllerReturn.of(40000, "文件已经存在");
        }
        
        try {
            String descr = faq.getFaqDescr();
            
            descr = descr.replaceAll("<m>", "\n");
            descr = descr.replaceAll("<mm>", "\n");
            
            descr = descr.replaceAll("</m>", "\n");
            descr = descr.replaceAll("</mm>", "\n");
            
            
            // ![](/img/faq/v1/493e8300-b753-4f83-9061-d8759778bdcb.png)
            
            Pattern p = Pattern.compile("/img/faq/v[0-9]+/(.*png)");
            Matcher m = p.matcher(descr);

            while (m.find()) {
                Path sourcePath = Paths.get(srcImgPath + "/" + m.group(1));
                Path targetPath = Paths.get(imgPath + "/" + m.group(1));
                if (!Files.exists(targetPath)) {
                    Files.copy(sourcePath, targetPath);  
                }
            }
            
            descr = descr.replaceAll("/img/faq/v[0-9]+/", "./img/");
            
            
            List<String> subList = faq.getSub();
            for (int i = 0; i < subList.size(); i++) {
                descr = descr.replaceFirst("<sub" + i + "/>", subList.get(i));
            }
            
            
            
            Files.write(mdFilePath, descr.getBytes());
            
            
            
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        return ControllerReturn.of();
    }
}
