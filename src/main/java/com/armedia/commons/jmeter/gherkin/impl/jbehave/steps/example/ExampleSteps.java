/*******************************************************************************
 * #%L
 * Armedia JMeter Gherkin Plugin
 * %%
 * Copyright (C) 2020 Armedia, LLC
 * %%
 * This file is part of the ArkCase software. 
 * 
 * If the software was purchased under a paid ArkCase license, the terms of 
 * the paid license agreement will prevail.  Otherwise, the software is 
 * provided under the following open source license terms:
 * 
 * ArkCase is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *  
 * ArkCase is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with ArkCase. If not, see <http://www.gnu.org/licenses/>.
 * #L%
 *******************************************************************************/
package com.armedia.commons.jmeter.gherkin.impl.jbehave.steps.example;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import com.armedia.commons.jmeter.gherkin.impl.jbehave.steps.BasicSteps;

public class ExampleSteps extends BasicSteps {

	@Given("this is a test")
	public void given() throws Throwable {
		"".hashCode();
	}

	@When("we want to debug")
	public void when() throws Throwable {
		"".hashCode();
	}

	@Then("start debugging")
	public void then() throws Throwable {
		"".hashCode();
		throw new RuntimeException("KABOOM!! WE FAILED HERE");
	}

}