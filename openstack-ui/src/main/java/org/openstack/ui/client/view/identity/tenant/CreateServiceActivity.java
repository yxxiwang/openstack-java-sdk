package org.openstack.ui.client.view.identity.tenant;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class CreateServiceActivity extends AbstractActivity {

	private static final CreateServiceView VIEW = new CreateServiceView();

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		panel.setWidget(VIEW);
	}

}
