package com.baodian.service.handover;

import java.text.ParseException;

import com.baodian.model.handover.SpecialTransfer;
import com.baodian.util.page.Page;

public interface SpecialTransferManager {
	
	public String specialTransfer_getuser();
	
	public String specialTransfer_add(SpecialTransfer specialtransfer);

	public String specialTransfer_del(SpecialTransfer specialtransfer);
	
	public String specialTransfer_update(SpecialTransfer specialtransfer);
	
	public String specialTransfer_show(Page page) throws ParseException;
	
}
