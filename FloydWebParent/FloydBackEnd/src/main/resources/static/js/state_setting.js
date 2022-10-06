var buttonLoadCountriesForStates;
var dropDownCountriesForStates;
var dropDownStates;

var buttonAddState;
var buttonUpdateState;
var buttonDeleteState;

var labelStateName;
var fieldStateName;

$(document).ready(function() {
    buttonLoadCountriesForStates = $("#buttonLoadCountriesForStates");
    dropDownCountriesForStates = $("#dropDownCountriesForStates");
    dropDownStates = $("#dropDownStates");

    buttonAddState = $("#buttonAddState");
    buttonUpdateState = $("#buttonUpdateState");
    buttonDeleteState = $("#buttonDeleteState");

    labelStateName = $("#labelStateName");
    fieldStateName = $("#fieldStateName");

    buttonLoadCountriesForStates.on("click", function() {
        loadCountriesForStates();
    });

    dropDownCountriesForStates.on("change", function() {
        loadStates();
    });

    dropDownStates.on("change", function() {
        changeFormStateToSelectedState();
    });

    buttonAddState.on("click", function() {
        if (buttonAddState.val() == "Add") {
            addState();
        }
        else {
            changeFormStateToNewForStates();
        }
    });

    buttonUpdateState.on("click", function() {
        updateState();
    });

    buttonDeleteState.on("click", function() {
        deleteState();
    });
});

function deleteState() {
    optionValue = dropDownStates.val()
    stateId = optionValue.split("-")[0];
    url = contextPath + "states/delete/" + stateId;

    $.get(url, function() {
        $("#dropDownStates option[value='" + optionValue + "']").remove();
    }).done(function() {
        showToastMessage("The state has been deleted");
        changeFormStateToNewForStates();
    }).fail(function() {
        showToastMessage("ERROR: Could not connect to the server");
    });
}

function updateState() {
    url = contextPath + "states/save";
    stateId = dropDownStates.val().split("-")[0];
    stateName = fieldStateName.val();

    selectedCountry = $("#dropDownCountriesForStates option:selected");
    countryId = selectedCountry.val().split("-")[0];
    countryName = selectedCountry.text();

    jsonData = {id: stateId, name: stateName, country: {id: countryId, name: countryName}};

    $.ajax({
        type: 'POST',
        url: url,
        data: JSON.stringify(jsonData),
        contentType: 'application/json',
        beforeSend: function(xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }
    }).done(function(stateId) {
        $("#dropDownStates option:selected").text(stateName);
        showToastMessage("The state has been updated");
        fieldStateName.val("").focus();
        changeFormStateToNewForStates();
    }).fail(function() {
        showToastMessage("ERROR: Could not connect to the server");
    });
}

function addState() {
    url = contextPath + "states/save";
    stateName = fieldStateName.val();
    selectedCountry = $("#dropDownCountriesForStates option:selected");

    countryId = selectedCountry.val().split("-")[0];
    countryName = selectedCountry.text();
    jsonData = {name: stateName, country: {id: countryId, name: countryName}};

    $.ajax({
        type: 'POST',
        url: url,
        data: JSON.stringify(jsonData),
        contentType: 'application/json',
        beforeSend: function(xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }
    }).done(function(stateId) {
        selectNewlyAddedState(stateId, stateName);
        showToastMessage("The new state has been added");
        fieldStateName.val("").focus();
    }).fail(function() {
        showToastMessage("ERROR: Could not connect to the server");
    });
}

function selectNewlyAddedState(stateId, stateName) {
    optionValue = stateId;
    $("<option>").val(optionValue).text(stateName).appendTo(dropDownStates);
    $("#dropDownStates option[value='" + optionValue + "']").prop("selected", true);
}

function changeFormStateToNewForStates() {
    buttonAddState.val("Add");
    labelStateName.text("State Name:")

    buttonUpdateState.prop("disabled", true);
    buttonDeleteState.prop("disabled", true);

    fieldStateName.val("").focus();
}

function changeFormStateToSelectedState() {
    buttonAddState.val("New");
    buttonUpdateState.prop("disabled", false);
    buttonDeleteState.prop("disabled", false);

    selectedStateName = $("#dropDownStates option:selected").text();

    labelStateName.text("Selected State:");
    fieldStateName.val(selectedStateName);
}

function loadStates() {
    countryId = dropDownCountriesForStates.val().split("-")[0];
    url = contextPath + "states/list/" + countryId;
    $.get(url, function(responseJSON) {
        dropDownStates.empty();
        $.each(responseJSON, function(index, state) {
            optionValue = state.id;
            $("<option>").val(optionValue).text(state.name).appendTo(dropDownStates);
        });
    }).done(function() {
        showToastMessage("All states has been loaded!");
    }).fail(function() {
        showToastMessage("ERROR: Could not connect to the server");
    });
}

function loadCountriesForStates() {
    url = contextPath + "countries/list";
    $.get(url, function(responseJSON) {
        dropDownCountriesForStates.empty();
        $.each(responseJSON, function(index, country) {
            optionValue = country.id + "-" + country.code;
            $("<option>").val(optionValue).text(country.name).appendTo(dropDownCountriesForStates);
        });
    }).done(function() {
        buttonLoadCountriesForStates.val("Refresh Country List");
        showToastMessage("All countries has been loaded!");
    }).fail(function() {
        showToastMessage("ERROR: Could not connect to the server");
    });
}

function showToastMessage(message) {
    $("#toastMessage").text(message);
    $(".toast").toast('show');
}