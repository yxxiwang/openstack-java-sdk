package org.openstack.api.compute;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Target;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.openstack.api.common.Resource;
import org.openstack.api.compute.ext.FloatingIpsResource;
import org.openstack.api.compute.ext.SecurityGroupsResource;
import org.openstack.model.compute.NovaSecurityGroupList;
import org.openstack.model.compute.NovaServer;
import org.openstack.model.compute.server.action.AddFixedIpAction;
import org.openstack.model.compute.server.action.AddFloatingIpAction;
import org.openstack.model.compute.server.action.ChangePasswordAction;
import org.openstack.model.compute.server.action.ConfirmResizeAction;
import org.openstack.model.compute.server.action.Console;
import org.openstack.model.compute.server.action.CreateBackupAction;
import org.openstack.model.compute.server.action.ForceDeleteAction;
import org.openstack.model.compute.server.action.GetConsoleOutputAction;
import org.openstack.model.compute.server.action.GetVncConsoleAction;
import org.openstack.model.compute.server.action.InjectNetworkInfoAction;
import org.openstack.model.compute.server.action.LockAction;
import org.openstack.model.compute.server.action.MigrateAction;
import org.openstack.model.compute.server.action.Output;
import org.openstack.model.compute.server.action.PauseAction;
import org.openstack.model.compute.server.action.RebootAction;
import org.openstack.model.compute.server.action.RebuildAction;
import org.openstack.model.compute.server.action.RemoveFixedIpAction;
import org.openstack.model.compute.server.action.RemoveFloatingIpAction;
import org.openstack.model.compute.server.action.ResetNetworkAction;
import org.openstack.model.compute.server.action.ResizeAction;
import org.openstack.model.compute.server.action.RestoreAction;
import org.openstack.model.compute.server.action.ResumeAction;
import org.openstack.model.compute.server.action.RevertResize;
import org.openstack.model.compute.server.action.SuspendAction;
import org.openstack.model.compute.server.action.UnlockAction;
import org.openstack.model.compute.server.action.UnpauseAction;

public class ServerResource extends Resource {

	public static class IpsResource extends Resource {
		
		public IpsResource(Target target) {
			super(target);
		}

		public String get(Map<String, Object> properties, String networkId) {
			return target.path("/ips").request(MediaType.APPLICATION_JSON).get(String.class);
		}

	}

	public ServerResource(Target target) {
		super(target);
	}
	
	public NovaServer get() {
		return get(new HashMap<String, Object>());
	}

	public NovaServer get(HashMap<String, Object> properties) {
		return target.request(MediaType.APPLICATION_JSON).get(NovaServer.class);
	}
	
	public NovaServer put(HashMap<String, Object> properties, Entity<NovaServer> server) {
		return target.request(MediaType.APPLICATION_JSON).put(server, NovaServer.class);
	}

	public Response delete() {
		return target.request().delete();
	}

	/**
	 * Restore a previously deleted instance.
	 * 
	 */
	public void restore() {
		executeAction(String.class, new RestoreAction());
	}

	/**
	 * Force delete of instance before deferred cleanup.
	 * 
	 */
	public void forceDelete() {
		executeAction(String.class, new ForceDeleteAction());
	}

	/**
	 * Update the password for a server.
	 * 
	 * @param adminPass
	 */
	public void changePassword(String adminPass) {
		ChangePasswordAction changePasswordAction = new ChangePasswordAction();
		changePasswordAction.setAdminPass(adminPass);
		executeAction(String.class, changePasswordAction);
	}

	/**
	 * Reboot a server.
	 * 
	 * @param type
	 *            either REBOOT_SOFT for a software-level reboot, or REBOOT_HARD for a virtual power cycle hard reboot.
	 */
	public void reboot(String type) {
		RebootAction rebootAction = new RebootAction();
		executeAction(String.class, rebootAction);
	}

	/**
	 * Rebuild -- shut down and then re-image -- a server.
	 * 
	 * @param rebuildAction
	 */
	public void rebuild(RebuildAction rebuildAction) {
		executeAction(String.class, rebuildAction);
	}

	public void resize(ResizeAction resizeAction) {
		executeAction(String.class, resizeAction);
	}

	/**
	 * Confirm that the resize worked, thus removing the original server.
	 * 
	 */
	public void confirmResize() {
		executeAction(String.class, new ConfirmResizeAction());
	}

	/**
	 * Revert a previous resize, switching back to the old server.
	 * 
	 */
	public void revertResize() {
		executeAction(String.class, new RevertResize());
	}

	/**
	 * Snapshot a server.
	 * 
	 * @param name
	 *            Name to give the snapshot image
	 * @param metadata
	 *            to give newly-created image entity
	 * 
	 */
	public void createImage(String name, Map<String, String> metadata) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	/**
	 * Permit Admins to pause the server
	 * 
	 * @return
	 */
	public void pause() {
		executeAction(String.class, new PauseAction());
	}

	/**
	 * Permit Admins to unpause the server
	 * 
	 * @return
	 */
	public void unpause() {
		executeAction(String.class, new UnpauseAction());
	}

	/**
	 * Permit Admins to suspend the server
	 * 
	 * @return
	 */
	public void suspend() {
		executeAction(String.class, new SuspendAction());
	}
	/**
	 * Permit admins to resume the server from suspend
	 * 
	 * @return
	 */
	public void resume() {
		executeAction(String.class, new ResumeAction());
	}

	/**
	 * Resize a server's resources.
	 * 
	 * flavor: the :class:`Flavor` (or its ID) to resize to.
	 * 
	 * Until a resize event is confirmed with :meth:`confirm_resize`, the old server will be kept around and you'll be
	 * able to roll back to the old flavor quickly with :meth:`revert_resize`. All resizes are automatically confirmed
	 * after 24 hours.
	 */
	public void resize(String flavorId) {
		ResizeAction resizeAction = new ResizeAction();
		// resizeAction.setAutoDiskConfig(autoDiskConfig);
		resizeAction.setFlavorRef(flavorId);
		executeAction(String.class, resizeAction);
	}

	/**
	 * Migrate a server to a new host in the same zone.
	 * Permit admins to migrate a server to a new host
	 * 
	 */
	
	public void migrate() {
		executeAction(String.class, new MigrateAction());
	}

	/**
	 * Permit admins to reset networking on an server
	 * 
	 * @return
	 */
	public void resetNetwork() {
		executeAction(String.class, new ResetNetworkAction());
	}

	/**
	 * Permit admins to inject network info into a server
	 * 
	 * @return
	 */
	public void injectNetworkInfo() {
		executeAction(String.class, new InjectNetworkInfoAction());
	}

	/**
	 * Permit admins to lock a server
	 * 
	 * @
	 */
	public void lock() {
		executeAction(String.class, new LockAction());
	}

	/**
	 * Permit admins to unlock a server
	 * 
	 * @return
	 */
	public void unlock() {
		executeAction(String.class, new UnlockAction());
	}

	/**
	 * Allow Admins to view pending server actions
	 * 
	 * @return
	 */
	public String pendingActions() {
		return target.path("actions").request().get(String.class);
	}

	public String virtualInterfaces() {
		return target.path("os-virtual-interfaces").request().get(String.class);
	}

	public void createBackup(CreateBackupAction createBackupAction) {

	}

	/**
	 * Allow Admins to view server diagnostics through server action
	 * 
	 * @return
	 */
	public String diagnostics() {
		return target.path("diagnostics").request().get(String.class);
	}

	public IpsResource ips() {
		return path("ips", IpsResource.class);
	}

	public MetadataResource metadata() {
		return path("metadata", MetadataResource.class);
	}

	/**
	 * Adds an IP on a given network to an instance.
	 * 
	 * @return
	 */
	public String addFixedIp(String networkId) {
		AddFixedIpAction addFixedIpAction = new AddFixedIpAction();
		addFixedIpAction.setNetworkId(networkId);
		return executeAction(String.class, addFixedIpAction);
	}

	/**
	 * Removes an IP from an instance.
	 * 
	 * @return
	 */
	public String removeFixedIp(String address) {
		RemoveFixedIpAction removeFixedIpAction = new RemoveFixedIpAction();
		removeFixedIpAction.setAddress(address);
		return executeAction(String.class, removeFixedIpAction);
	}

	/**
	 * Attaches a floating IP to the instance.
	 */
	public void addFloatingIp(String ip) {
		AddFloatingIpAction action = new AddFloatingIpAction();
		action.setAddress(ip);
		executeAction(String.class, action);
	}

	/**
	 * Detaches a floating IP from the instance
	 */
	public void removeFloatingIp(String ip) {
		RemoveFloatingIpAction action = new RemoveFloatingIpAction();
		action.setAddress(ip);
		executeAction(String.class, action);
	}

	/**
	 * Get text console log output from Server.
	 * 
	 * @return
	 */
	public Console getVncConsole(String type) {
		GetVncConsoleAction action = new GetVncConsoleAction();
		action.setType(type);
		Console console = executeAction(Console.class, action);
		return console;
	}

	/**
	 * Get text console output.
	 * 
	 * @return
	 */
	public String getConsoleOutput(Integer length) {
		GetConsoleOutputAction action = new GetConsoleOutputAction();
		action.setLength(length);

		// XML output is not escaped correctly.  Bug #939386
		Output output = executeAction(Output.class, action, MediaType.APPLICATION_JSON_TYPE);
		return output.getOutput();
	}

	public <T> T executeAction(Class<T> c, Object action) {
		return executeAction(c, action, null);
	}

	private <T> T executeAction(Class<T> c, Object action, MediaType forceType) {
		return target.path("action").request(forceType).post(Entity.xml(action), c);
	}

	public void createAttachment() {
		// "os-volume_attachments"
	}

	public void delteAttachment() {
		// "os-volume_attachments"
	}

	public FloatingIpsResource floatingIps() {
		return path("os-floating-ips", FloatingIpsResource.class);
	}

	public ConsolesResource consoles() {
		return path("consoles", ConsolesResource.class);
	}

	public NovaSecurityGroupList listSecurityGroups() {
		return path("os-security-groups", SecurityGroupsResource.class).get(new HashMap<String, Object>());
	}
	
	/**
	 * Rescue an instance.
	 * 
	 * @return
	 */
	public String rescue() {
		return null;
	}

	/**
	 * Rescue an instance.
	 * 
	 * @return
	 */
	public String unrescue() {
		return null;
	}

	/**
	 * Backup a server instance.
	 * 
	 * Images now have an `image_type` associated with them, which can be 'snapshot' or the backup type, like 'daily' or
	 * 'weekly'.
	 * 
	 * If the image_type is backup-like, then the rotation factor can be included and that will cause the oldest backups
	 * that exceed the rotation factor to be deleted.
	 * 
	 * @return
	 */
	public String backup() {
		return null;
	}

	

}
