String.prototype.trim = function(){
	return this.replace(/^(\s|\u00A0)+/,'').replace(/(\s|\u00A0)+$/,'');   
}