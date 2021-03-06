package org.openstack.ui.client.view.identity.tenant;

import org.openstack.ui.client.UI;
import org.openstack.ui.client.view.identity.tenant.CreateUserView.Presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

public class CreateServiceView extends Composite {

	private static CreateServiceViewUiBinder uiBinder = GWT
			.create(CreateServiceViewUiBinder.class);

	interface CreateServiceViewUiBinder extends
			UiBinder<Widget, CreateServiceView> {
	}

	public interface Presenter {
		
	}
	
	private Presenter presenter;
	
	public CreateServiceView() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@UiHandler({ "cancel", "close" })
	public void onCancel(ClickEvent event) {
		UI.MODAL.hide(true);
	}

}
