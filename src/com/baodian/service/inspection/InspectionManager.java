package com.baodian.service.inspection;

import com.baodian.model.inspection.Inspection01;
import com.baodian.model.inspection.Inspection02;
import com.baodian.model.inspection.Inspection03;
import com.baodian.util.page.Page;

public interface InspectionManager {
	
	public String inspection_all_add(Inspection01 inspection01,Inspection02 inspection02,Inspection03 inspection03);

	public String inspection_show(Page page);
	
	public Inspection01 inspection01_get(String insp_id);
	public Inspection02 inspection02_get(String insp_id);
	public Inspection03 inspection03_get(String insp_id);
	
	public String inspection_del(String insp_id);
}
