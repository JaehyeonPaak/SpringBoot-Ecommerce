var buttonLoadCountries;
var dropDownCountries;
var buttonAddCountry;
var buttonUpdateCountry;
var buttonDeleteCountry;
var labelCountryName;
var fieldCountryName;
var fieldCountryCode;

$(document).ready(function() {
    buttonLoadCountries = $("#buttonLoadCountries");
    dropDownCountries = $("#dropDownCountries");
    buttonAddCountry = $("#buttonAddCountry");
    buttonUpdateCountry = $("#buttonUpdateCountry");
    buttonDeleteCountry = $("#buttonDeleteCountry");
    labelCountryName = $("#labelCountryName");
    fieldCountryName = $("#fieldCountryName");
    fieldCountryCode = $("#fieldCountryCode");

    buttonLoadCountries.on("click", function() {
        loadCountries();
    });

    dropDownCountries.on("change", function() {
        changeFormStateToSelectedCountry();
    });

    buttonAddCountry.on("click", function() {
        if (buttonAddCountry.val() == "Add") {
            addCountry();
        }
        else {
            changeFormStateToNew();
        }
    });

    buttonUpdateCountry.on("click", function() {
        updateCountry();
    });

    buttonDeleteCountry.on("click", function() {
        deleteCountry();
    });
});

function deleteCountry() {
    optionValue = dropDownCountries.val()
    countryId = optionValue.split("-")[0];
    url = contextPath + "countries/delete/" + countryId;

    $.get(url, function() {
        $("#dropDownCountries option[value='" + optionValue + "']").remove();
        changeFormStateToNew();
    }).done(function() {
        showToastMessage("The country has been deleted");
    }).fail(function() {
        showToastMessage("ERROR: Could not connect to the server");
    });
}

function updateCountry() {
    url = contextPath + "countries/save";
    countryName = fieldCountryName.val();
    countryCode = fieldCountryCode.val();
    countryId = dropDownCountries.val().split("-")[0];

    jsonData = {id: countryId, name: countryName, code: countryCode};
    $.ajax({
        type: 'POST',
        url: url,
        data: JSON.stringify(jsonData),
        contentType: 'application/json',
        beforeSend: function(xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }
    }).done(function(countryId) {
        $("#dropDownCountries option:selected").val(countryId + "-" + countryCode);
        $("#dropDownCountries option:selected").text(countryName);
        showToastMessage("The country has been updated");
        fieldCountryName.val("").focus();
        fieldCountryCode.val("");
        changeFormStateToNew();
    }).fail(function() {
        showToastMessage("ERROR: Could not connect to the server");
    });
}

function addCountry() {
    url = contextPath + "countries/save";
    countryName = fieldCountryName.val();
    countryCode = fieldCountryCode.val();
    jsonData = {name: countryName, code: countryCode};
    $.ajax({
        type: 'POST',
        url: url,
        data: JSON.stringify(jsonData),
        contentType: 'application/json',
        beforeSend: function(xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }
    }).done(function(countryId) {
        selectNewlyAddedCountry(countryId, countryName, countryCode);
        showToastMessage("The new country has been added");
        fieldCountryName.val("").focus();
        fieldCountryCode.val("");
    }).fail(function() {
        showToastMessage("ERROR: Could not connect to the server");
    });
}

function selectNewlyAddedCountry(countryId, countryName, countryCode) {
    optionValue = countryId + "-" + countryCode;
    $("<option>").val(optionValue).text(countryName).appendTo(dropDownCountries);
    $("#dropDownCountries option[value='" + optionValue + "']").prop("selected", true);
}

function changeFormStateToNew() {
    buttonAddCountry.val("Add");
    labelCountryName.text("Country Name:")

    buttonUpdateCountry.prop("disabled", true);
    buttonDeleteCountry.prop("disabled", true);

    fieldCountryName.val("").focus();
    fieldCountryCode.val("");
}

function changeFormStateToSelectedCountry() {
    buttonAddCountry.val("New");
    buttonUpdateCountry.prop("disabled", false);
    buttonDeleteCountry.prop("disabled", false);

    selectedCountryName = $("#dropDownCountries option:selected").text();
    selectedCountryCode = dropDownCountries.val().split("-")[1];

    labelCountryName.text("Selected Country:");
    fieldCountryName.val(selectedCountryName);
    fieldCountryCode.val(selectedCountryCode);
}

function loadCountries() {
    url = contextPath + "countries/list";
    $.get(url, function(responseJSON) {
        dropDownCountries.empty();
        $.each(responseJSON, function(index, country) {
            optionValue = country.id + "-" + country.code;
            $("<option>").val(optionValue).text(country.name).appendTo(dropDownCountries);
        });
    }).done(function() {
        buttonLoadCountries.val("Refresh Country List");
        showToastMessage("All countries has been loaded!");
    }).fail(function() {
        showToastMessage("ERROR: Could not connect to the server");
    });
}

function showToastMessage(message) {
    $("#toastMessage").text(message);
    $(".toast").toast('show');
}