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
package com.armedia.commons.jmeter.gherkin.config;

import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;

import com.armedia.commons.jmeter.gherkin.GherkinTools;
import com.armedia.commons.jmeter.gherkin.impl.jbehave.JBehaveRunner;
import com.armedia.commons.jmeter.gherkin.impl.jbehave.JBehaveSettings;

class JBehaveEngine extends GherkinEngine {

	public static final String DEFAULT_SYNTAX = JBehaveSettings.defaults().getSyntax().name();
	public static final String DEFAULT_OUTPUT_FORMAT = JBehaveSettings.defaults().getOutputFormat().name();

	private final JBehaveSettings settings = new JBehaveSettings();
	private JBehaveRunner runner = null;

	public String getOutputFormat() {
		return this.settings.getOutputFormat().name();
	}

	public JBehaveEngine setOutputFormat(String outputFormat) {
		JBehaveSettings.OutputFormat of = null;
		if (outputFormat != null) {
			try {
				of = JBehaveSettings.OutputFormat.valueOf(outputFormat);
			} catch (IllegalArgumentException e) {
			}
		}
		this.settings.setOutputFormat(of);
		return this;
	}

	public String getSyntax() {
		return this.settings.getSyntax().name();
	}

	public JBehaveEngine setSyntax(String syntax) {
		JBehaveSettings.Syntax s = null;
		if (syntax != null) {
			try {
				s = JBehaveSettings.Syntax.valueOf(syntax);
			} catch (IllegalArgumentException e) {
			}
		}
		this.settings.setSyntax(s);
		return this;
	}

	public JBehaveEngine setFailOnPending(boolean failOnPending) {
		this.settings.setFailOnPending(failOnPending);
		return this;
	}

	public boolean isFailOnPending() {
		return this.settings.isFailOnPending();
	}

	public JBehaveEngine setDryRun(boolean dryRun) {
		this.settings.setDryRun(dryRun);
		return this;
	}

	public boolean isDryRun() {
		return this.settings.isDryRun();
	}

	@Override
	public void init(GherkinConfig config) throws Exception {
		// Populate these from the configuration
		Set<String> packagesToScan = new LinkedHashSet<>();
		String packages = config.getPackages();
		if (StringUtils.isNotBlank(packages)) {
			Function<String, String> map = (s) -> s.replaceAll("#.*", "");
			map = map.andThen(StringUtils::strip);
			Predicate<String> pred = StringUtils::containsWhitespace;
			pred = pred.negate();
			try (LineNumberReader r = new LineNumberReader(new StringReader(packages))) {
				r.lines() //
					.filter(StringUtils::isNotBlank) // avoid empty lines for speed
					.map(map) //
					.filter(pred) // Only include lines that don't contain whitespace
					.forEach(packagesToScan::add) //
				;
			}
		}

		// For now, we have a single composite - the declared one!
		Map<String, String> compositesMap = new HashMap<>();

		String composites = GherkinTools.getOverridableText(config.getCompositesFile(), config::getComposites);
		composites = GherkinTools.interpolateText(composites);
		compositesMap.put("declaredComposites", composites);

		this.runner = new JBehaveRunner(packagesToScan, compositesMap);
	}

	@Override
	public Result<?> runStory(String storyName, String story) throws Exception {
		return this.runner.run(storyName, story, this.settings);
	}

	@Override
	public void close() {
		this.runner = null;
	}
}