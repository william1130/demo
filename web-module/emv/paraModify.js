function _revisedMonitor() {
	var fieldObjs = [];						
	this.fieldObjs = fieldObjs;
	
	this.addField = function(obj, name) {
		var origObj = new origFieldObj(obj, name);			
		origObj.setValue(getObjValue(obj));		
		
		$(obj).change(function(){
			if (getObjValue(obj) != origObj.getValue()) {
				origObj.setChange("Y");
			} else {
				origObj.setChange("N");
			}
		});
		
		fieldObjs.push(origObj);
	};
	
	this.result = function() {
		var ary = [];
		for (var i = 0 ; i < fieldObjs.length; i++) {
			if (fieldObjs[i].getIsChange() == "Y") {
				ary.push(fieldObjs[i].chName + ":" + getObjValue(fieldObjs[i].obj)) + " ";
			}
		}
		
		return ary.join();
	};	
	
	function getObjValue(obj) {
		var rv;
		if ($j(obj).prop("tagName") == "INPUT" && $j(obj).attr("type") == "checkbox" ) {
			rv = $j(obj).prop("checked") ? "Y" : "N";			
		} else {
			rv = obj.val();
		}
		
		return rv;
	}
}					

function origFieldObj(obj, chName) {
	var value = null;
	var isChange = "N";
	
	this.obj = obj;
	this.chName = chName;							
	
	this.setValue = function(v) {value = v};
	this.getValue = function() {return value};
	this.setChange = function(v) {isChange = v};
	this.getIsChange = function() {return isChange};
}	
										
$revisedMonitor = new _revisedMonitor();