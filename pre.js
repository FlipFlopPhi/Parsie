function prepDocument() {
	//document.head.setAtrribute('Access-Control-Allow-Origin','*');
	for(var i=0; i<document.styleSheets.length; i++) {
		var css = document.styleSheets[i];
		if (css.href != null) {
			loadCSSCors(css.href,css.ownerNode);
		}
	}
}

/**
* The following function has been taken from: http://stackoverflow.com/questions/3211536/accessing-cross-domain-style-sheet-with-cssrules
* This function was designed by Jordan M Alperin
*/
function loadCSSCors(stylesheet_uri, e) {
	var _xhr = XMLHttpRequest;
	var has_cred = false;
	try {has_cred = _xhr && ('withCredentials' in (new _xhr()));} catch(e) {}
	if (!has_cred) {
		return 'CORS not supported';
	}
	var xhr = new _xhr();
	xhr.open('GET', stylesheet_uri,false);
	xhr.onload = function() {
		xhr.onload = xhr.onerror = null;
		if (xhr.status < 200 || xhr.status >=300) {
			console.error('style failed to load: ' + stylesheet_uri)
		} else {
			var style_tag = document.createElement('style');
			style_tag.appendChild(document.createTextNode(xhr.responseText));
			document.head.replaceChild(style_tag,e)
		}
	};
	xhr.onerror = function() {
		xhr.onload = xhr.onerror = null;
		console.error('XHR CORS CSS fail:' + styleURI);
	};
	xhr.send();
}

prepDocument();