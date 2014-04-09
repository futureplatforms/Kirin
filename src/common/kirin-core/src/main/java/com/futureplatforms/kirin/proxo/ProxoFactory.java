package com.futureplatforms.kirin.proxo;

public class ProxoFactory {
	private String _Hostname, _Prefix;
	/**
	 * Use me to retrieve several Proxo sheets on a single host
	 * @param hostname
	 * @param prefix
	 */
	public ProxoFactory(String hostname, String prefix) {
		this._Hostname = hostname;
		this._Prefix = prefix;
	}
	
	public Proxocube getProxocube(String suffix, ProxoClient client) {
		return this.getProxocube(suffix, client, null);
	}
	
	public Proxocube getProxocube(String suffix, ProxoClient client, String bakage) {
		return new Proxocube(_Hostname + _Prefix + suffix, client, bakage);
	}
}
