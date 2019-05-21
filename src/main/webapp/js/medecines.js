var MEDECINES_SERVLET = "/DocsAdmin/medecines";

var IS_REM_CHILD = '<input type="checkbox" class="filled-in" checked="checked" />' +
                   '<label for="nothing"></label>';
var IS_NOT_REM_CHILD = '<input type="checkbox" />' +
                       '<label for="nothing"></label>';

function addMedecine(dci, mark, form, dosage, isRemb){    
    $('#medecinesTable')
        .append($('<tr>')
        .append($('<td class="dataCell">').append($("#medecinesTable tr").length))
        .append($('<td class="dataCell">').append(dci))
        .append($('<td class="dataCell">').append(mark))
        .append($('<td class="dataCell">').append(form))
        .append($('<td class="dataCell">').append(dosage))
        .append($('<td class="dataCell">').append(isRemb === 'true' ? IS_REM_CHILD : IS_NOT_REM_CHILD))
    );
    
}

function removeAllMedecines(){
    $('#medecinesTable tbody tr').remove();
}

function refreshMedecinesFromServer(requestContent){
    $.post(
        MEDECINES_SERVLET, 
        {
            TARGETED_MEDECINES: requestContent
        },
        function(result){
            // Le resultat est un fichier xml
        	removeAllMedecines();
            
            var xmlDoc = $.parseXML(result);
            var xml = $(xmlDoc);
            
            $(xml).find("medecine").each( function(){
                var dci = $(this).find("medecineDci").text();
                var mark = $(this).find("medecineMark").text();
                var form = $(this).find("medecineForm").text();
                var dosage = $(this).find("medecineDosage").text();
                var redeem = $(this).find("medecineRedeemability").text();
                
                addMedecine(dci, mark, form, dosage, redeem);
            });
        }
    ).fail(function() {
        alert("Impossible de rafraichir la liste des médicaments... Veuillez réessayer plus tard.");
    });
}

(function(){
    $("#medecinesSearchInput").keyup(function(){
        refreshMedecinesFromServer($(this).val());
    });
})();
