package sy.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;





import sy.dao.TextBoxDaoI;
import sy.model.Ttextbox;
import sy.pageModel.SessionInfo;
import sy.pageModel.TextBox;
import sy.pageModel.DataGrid;
import sy.pageModel.PageHelper;
import sy.pageModel.TextBox;
import sy.service.TextBoxServiceI;
import sy.service.TextBoxServiceI;
import sy.util.ClobUtil;

@Service
public class TextBoxImpl implements TextBoxServiceI {

	@Autowired
	private TextBoxDaoI textboxDao;


	@Override
	public DataGrid dataGrid(TextBox TextBox, PageHelper ph) {
		DataGrid dg = new DataGrid();
		List<TextBox> bl = new ArrayList<TextBox>();
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from Ttextbox t ";
		List<Ttextbox> l = textboxDao.find(hql+ whereHql(TextBox, params) + orderHql(ph), params, ph.getPage(), ph.getRows());
		if (l != null && l.size() > 0) {
			for (Ttextbox t : l) {
				TextBox b = new TextBox();
				BeanUtils.copyProperties(t, b);
	
				bl.add(b);
			}
		}
		
		dg.setTotal(textboxDao.count("select count(*) " + hql + whereHql(TextBox, params), params));
		dg.setRows(bl);
		
		return dg;
	}

	private String orderHql(PageHelper ph) {
		String orderString = "";
		if (ph.getSort() != null && ph.getOrder() != null) {
			orderString = " order by t." + ph.getSort() + " " + ph.getOrder();
		}
		return orderString;
	}

	private String whereHql(TextBox TextBox, Map<String, Object> params) {
		String whereHql = "";
		if (TextBox != null) {
			whereHql += " where 1=1 ";
			if (TextBox.getType() != null) {
				whereHql += " and t.type like :type";
				params.put("type", "%%" + TextBox.getType() + "%%");
			}
			
			if (TextBox.getName() != null) {
				whereHql += " and t.name like :name";
				params.put("name", "%%" + TextBox.getName() + "%%");
			}
			
		}
		return whereHql;
	}

	@Override
	public void add(TextBox TextBox, SessionInfo sessionInfo) {
		Ttextbox t = new Ttextbox();
		BeanUtils.copyProperties(TextBox, t);
	    t.setBoundary(TextBox.getBoundary_down()+","+TextBox.getBoundary_up());
	    t.setBlank(TextBox.getBlank_q()+","+TextBox.getBlank_z()+","+TextBox.getBlank_h());
	    t.setLetter(TextBox.getLetter_d()+","+TextBox.getLetter_x()+","+TextBox.getLetter_m());
	    t.setNumber(TextBox.getNumber_i()+"");
	    t.setNumber(TextBox.getNumber_i()+","+TextBox.getNumber_d());
	    t.setUpdatetime(new Date());
	 			t.setUpdater(sessionInfo.getName());
		textboxDao.save(t);
		}

	@Override
	public TextBox get(String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		Ttextbox t = textboxDao.get("from Ttextbox t where t.id = :id", params);
		TextBox b = new TextBox();
		BeanUtils.copyProperties(t, b);
		String[] blank=t.getBlank().split(",");
		if(blank.length>0){
			b.setBlank_q(blank[0]);
			b.setBlank_z(blank[1]);
			b.setBlank_h(blank[2]);
		}
		
		String[] letter=t.getLetter().split(",");
		if(letter.length>0){
			b.setLetter_d(letter[0]);
			b.setLetter_x(letter[1]);
			b.setLetter_m(letter[2]);
		}
		
		String[] Boundary=t.getBoundary().split(",");
		if(Boundary.length>0){
			b.setBoundary_down(Boundary[0]);
			b.setBoundary_up(Boundary[1]);
		}
		
		String[] num=t.getNumber().split(",");
		if(num.length>0){
			b.setNumber_i(num[0]);
			b.setNumber_d(num[1]);
		}

		return b;
	}

	@Override
	public void edit(TextBox TextBox, SessionInfo sessionInfo) {
		Ttextbox t = textboxDao.get(Ttextbox.class, TextBox.getId());
		if (t != null) {
			BeanUtils.copyProperties(TextBox, t);	
		    t.setBoundary(TextBox.getBoundary_down()+","+TextBox.getBoundary_up());
		    t.setBlank(TextBox.getBlank_q()+","+TextBox.getBlank_z()+","+TextBox.getBlank_h());
		    t.setLetter(TextBox.getLetter_d()+","+TextBox.getLetter_x()+","+TextBox.getLetter_m());
		    t.setNumber(TextBox.getNumber_i()+","+TextBox.getNumber_d());

			t.setUpdater(sessionInfo.getName());
			t.setUpdatetime(new Date());
		}
	}

	@Override
	public void delete(String id) {
		textboxDao.delete(textboxDao.get(Ttextbox.class, id));
	}

}
