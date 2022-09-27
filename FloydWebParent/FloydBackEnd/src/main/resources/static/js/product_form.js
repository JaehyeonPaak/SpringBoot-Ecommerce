  var extraImageCount = 0;

  $(document).ready(function() {
    $("#buttonCancel").on("click", function() {
        window.location = moduleURL;
    });

    $("#fileImage").change(function() {
        fileSize = this.files[0].size;
        if (fileSize > 1048576) {
            this.setCustomValidity("You must choose an image less than 1MB!");
            this.reportValidity();
        }
        else {
            this.setCustomValidity("");
            showImageThumbnail(this);
        }
    });

    $("input[name='extraImage']").each(function(index) {
        extraImageCount++;
        $(this).change(function() {
            showExtraImageThumbnail(this, index);
        });
    });
  });

  function showImageThumbnail(fileInput) {
   var file = fileInput.files[0];
   var reader = new FileReader();
   reader.onload = function(e) {
     $("#thumbnail").attr("src", e.target.result);
   };
   reader.readAsDataURL(file);
  }

  function showExtraImageThumbnail(fileInput, index) {
     var file = fileInput.files[0];

     fileName = file.name;
     imageNameHiddenField = $("#imageName" + index);
     if (imageNameHiddenField.length) {
        imageNameHiddenField.val(fileName);
     }

     var reader = new FileReader();
     reader.onload = function(e) {
       $("#extraThumbnail" + index).attr("src", e.target.result);
     };
     reader.readAsDataURL(file);

     if (index >= extraImageCount - 1) {
        addNextExtraImageSection(index + 1);
     }
  }

  function addNextExtraImageSection(index) {
    index1 = index + 1;
    htmlExtraImage = '<div class="col border m-3 p-2" id="divExtraImage' + index + '">' +
           '<div id="extraImageHeader' + index + '"><label>Extra Image #' + index1 + ':</label></div>' +
           '<img id="extraThumbnail' + index + '" alt="Extra Image #' + index1 + ' Preview" class="img-fluid" th:src="@{/images/image-thumbnail.png}">' +
           '<div><input type="file" name="extraImage" accept="image/png, image/jpeg" onchange="showExtraImageThumbnail(this, ' + index + ')"></div>' +
           '</div>';

    index2 = index - 1;
    htmlLinkRemove = '<a class="btn fas fa-times-circle fa-2x icon-dark float-right" title="Remove this image" href="javascript:removeExtraImage(' + index2 + ')"></a>';

    $("#divProductImages").append(htmlExtraImage);

    $("#extraImageHeader" + (index - 1)).append(htmlLinkRemove);
    extraImageCount++;
  }

  function removeExtraImage(index) {
    $("#divExtraImage" + index).remove();
  }

