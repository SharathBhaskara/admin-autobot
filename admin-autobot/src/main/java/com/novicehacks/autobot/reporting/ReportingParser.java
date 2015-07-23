package com.novicehacks.autobot.reporting;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Place Holder class for reporting feature.
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks!
 *
 */

public interface ReportingParser {

	public List<CommandOutputBean> readCommandOutput();

	public Set<ReportingBean> reportingBeanList(Collection<CommandOutputBean> outputBeanCollection);

}
