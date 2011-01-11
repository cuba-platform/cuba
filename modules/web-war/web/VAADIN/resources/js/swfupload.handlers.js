function setProgress(progress, text){
    progress.style.visibility = "visible";
    progress.style.display = "table";
    progress.style.height = "50px";
    progress.style.textAlign = "center";

    removeChilds(progress);

    var progressText = document.createElement("div");
    progressText.setAttribute("style","");
    progressText.style.display = "table-cell";
    progressText.style.verticalAlign = "middle";

    progressText.appendChild(document.createTextNode(text));
    progress.appendChild(progressText);
}

function removeChilds(element){
    if ( element.hasChildNodes() )
        while ( element.childNodes.length >= 1 )
            element.removeChild( element.firstChild );
}

function fileQueued(file) {

}

function fileDialogComplete(numFilesSelected, numFilesQueued) {
	try {
		if (numFilesSelected > 0) {
		    this.startUpload();
		}
	} catch (ex)  {
        this.debug(ex);
	}
}

function uploadStart(file) {
	try {
        var progress = document.getElementById(this.customSettings.progressTarget);
        setProgress(progress,file.name);
	}
	catch (ex) {}

	return true;
}

function uploadProgress(file, bytesLoaded, bytesTotal) {
	try {
		var percent = Math.ceil((bytesLoaded / bytesTotal) * 100);
        var progress = document.getElementById(this.customSettings.progressTarget);
        setProgress(progress,file.name + " " + percent + "%");
	} catch (ex) {
		this.debug(ex);
	}
}

function uploadSuccess(file, serverData) {
	try {
        var progress = document.getElementById(this.customSettings.progressTarget);
        setProgress(progress,file.name + " OK");
	} catch (ex) {
		this.debug(ex);
	}
}

var swfUploadHelper = {
    create : function(opts) {
        return new SWFUpload(opts);
    }
}