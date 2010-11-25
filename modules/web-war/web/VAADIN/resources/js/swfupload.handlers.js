function fileDialogComplete(numFilesSelected, numFilesQueued) {
    try {
        if (this.getStats().files_queued > 0) {
            this.startUpload();
        }
    } catch (ex) {
        this.debug(ex);
    }
}

var swfUploadHelper = {
    create : function(opts) {
        return new SWFUpload(opts);
    }
};

function uploadError(file, errorCode, message) {
    try {
        var progress = new FileProgress(file, this.customSettings.progressTarget);
        progress.setError();
        progress.toggleCancel(false);

        switch (errorCode) {
            case SWFUpload.UPLOAD_ERROR.HTTP_ERROR:
                progress.setStatus("Upload Error: " + message);
                this.debug("Error Code: HTTP Error, File name: " + file.name + ", Message: " + message);
                break;
            case SWFUpload.UPLOAD_ERROR.UPLOAD_FAILED:
                progress.setStatus("Upload Failed.");
                this.debug("Error Code: Upload Failed, File name: " + file.name + ", File size: " + file.size + ", Message: " + message);
                break;
            case SWFUpload.UPLOAD_ERROR.IO_ERROR:
                progress.setStatus("Server (IO) Error");
                this.debug("Error Code: IO Error, File name: " + file.name + ", Message: " + message);
                break;
            case SWFUpload.UPLOAD_ERROR.SECURITY_ERROR:
                progress.setStatus("Security Error");
                this.debug("Error Code: Security Error, File name: " + file.name + ", Message: " + message);
                break;
            case SWFUpload.UPLOAD_ERROR.UPLOAD_LIMIT_EXCEEDED:
                progress.setStatus("Upload limit exceeded.");
                this.debug("Error Code: Upload Limit Exceeded, File name: " + file.name + ", File size: " + file.size + ", Message: " + message);
                break;
            case SWFUpload.UPLOAD_ERROR.FILE_VALIDATION_FAILED:
                progress.setStatus("Failed Validation.  Upload skipped.");
                this.debug("Error Code: File Validation Failed, File name: " + file.name + ", File size: " + file.size + ", Message: " + message);
                break;
            case SWFUpload.UPLOAD_ERROR.FILE_CANCELLED:
                // If there aren't any files left (they were all cancelled) disable the cancel button
                if (this.getStats().files_queued === 0) {
                    document.getElementById(this.customSettings.cancelButtonId).disabled = true;
                }
                progress.setStatus("Cancelled");
                progress.setCancelled();
                break;
            case SWFUpload.UPLOAD_ERROR.UPLOAD_STOPPED:
                progress.setStatus("Stopped");
                break;
            default:
                progress.setStatus("Unhandled Error: " + errorCode);
                this.debug("Error Code: " + errorCode + ", File name: " + file.name + ", File size: " + file.size + ", Message: " + message);
                break;
        }
    } catch (ex) {
        this.debug(ex);
    }
}