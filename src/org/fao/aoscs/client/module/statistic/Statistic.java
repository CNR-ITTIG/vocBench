package org.fao.aoscs.client.module.statistic;

import org.fao.aoscs.client.module.statistic.widgetlib.StatisticsView;

import com.google.gwt.user.client.ui.Composite;

public class Statistic extends Composite{
	
	public Statistic()
	{
		StatisticsView statView = new StatisticsView();
		initWidget(statView);
	}
}
