package com.dal.catmeclone.ValidationTest;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import com.dal.catmeclone.Validation.MaximumLength;
import com.dal.catmeclone.Validation.ValidationPolicy;
import com.dal.catmeclone.model.User;

public class HistoryConstraintTest {
	
	HistoryConstraintMock HistoryConstraintMock =new HistoryConstraintMock();

	@Test
	public void TestHistoryConstraint() throws Exception {
		User u=new User();
		
		//Valid
		u.setPassword("Hello");
		HistoryConstraintMock.setValue("3");
		Assert.isTrue(HistoryConstraintMock.isValid(u));
		
		//Invalid
		u.setPassword("Password@123");
		HistoryConstraintMock.setValue("2");
		Assert.isTrue(!HistoryConstraintMock.isValid(u));
	
	}
}