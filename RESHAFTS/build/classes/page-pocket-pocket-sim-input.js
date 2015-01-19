function PocketUiCollection(
	jmolSuffix,
	jmolPanelId,
	jmolContainerId,
	buttonLoadId,
	buttonAnalyzeId,
	inputPdbCodeId,
	inputResidueSelectorId,
	selectChainId,
	selectLigandId,
	selectDistanceId,
	sequencePanelId,
	sequenceContainerId
	) {
	this.jmolSuffix = jmolSuffix;
	this.jmolPanelId = jmolPanelId;
	this.jmolContainerId = jmolContainerId;
	this.buttonLoadId = buttonLoadId;
	this.buttonAnalyzeId = buttonAnalyzeId;
	this.inputPdbCodeId = inputPdbCodeId;
	this.inputResidueSelectorId = inputResidueSelectorId;
	this.selectChainId = selectChainId;
	this.selectLigandId = selectLigandId;
	this.selectDistanceId = selectDistanceId;
	this.sequencePanelId = sequencePanelId;
	this.sequenceContainerId = sequenceContainerId;
}


function generateJmolLoadButtonClickHandler(pocketUiCollection) {
	return function() {
		var suffixJmol = pocketUiCollection.jmolSuffix;
		var jmolPanelId = "#" + pocketUiCollection.jmolPanelId;
		var jmolContainerId = "#" + pocketUiCollection.jmolContainerId;
		var inputPdbCodeId = "#" + pocketUiCollection.inputPdbCodeId;
		var sequencePanelId = "#" + pocketUiCollection.sequencePanelId;
		
		$(jmolPanelId).show();
		$(sequencePanelId).show();
		
		// Get input.
		var pdbCode = $(inputPdbCodeId).val().toLowerCase();
		
		// script for Jmol
		var script = "load " + ctx + "/pdb-repository/" + "pdb" + pdbCode + ".ent.gz;";
		script += "define myChain all;";
		script += "ligandResidueIds={hetero and !(water)}.label('%[resno]');";
		script += "proteinResidueMap={protein and *.ca}.label('\"%[resno]\": \"%[group]\"');";
		script += "proteinResidueIds={protein and *.ca}.label('%[resno]');";
		script += "proteinChainIds={protein and *.ca}.label('%[chain]')";
		
		//jmolScriptWait(script, suffixJmol);
		$(jmolContainerId).empty();
		jmolSetAppletColor("#EEEEEE");
		jmolSetXHTML(jmolContainerId.substring(1));
		jmolApplet(400, script, suffixJmol);
	};
}


function generateButtonAnalyzeClickHandler(pocketUiCollection) {
	return function () {
		var suffixJmol = pocketUiCollection.jmolSuffix;
		var selectChainId = "#" + pocketUiCollection.selectChainId;
		var selectLigandId = "#" + pocketUiCollection.selectLigandId;
		var sequencePanelId = "#" + pocketUiCollection.sequencePanelId;
		var sequenceContainerId = "#" + pocketUiCollection.sequenceCotainerId;
		
		
		// protein chain ID array
		var proteinChainIds = jmolEvaluate("proteinChainIds", suffixJmol);
		proteinChainIds = proteinChainIds.replace(/[ \r\t]/g, "");
		proteinChainIds = proteinChainIds.replace(/(.+)/g, "'$1'");
		proteinChainIds = proteinChainIds.replace(/\n/g, ", ");
		proteinChainIds = "([" + proteinChainIds + "])";
		proteinChainIds = eval(proteinChainIds);
		proteinChainIds = filterDuplicate(proteinChainIds);
		proteinChainIds.sort();
		
		
		/* Update chain selector. */
		jChainSelector = $(selectChainId);
		jChainSelector.empty();
		for (var i in proteinChainIds) {
			var chainId = proteinChainIds[i];
			$("<option/>").val(chainId).text("Chain: " + chainId).appendTo(jChainSelector);
		}
		
		updateSelectUi(pocketUiCollection);
		updateSequenceUi(pocketUiCollection);
	};
}


function generateSelectLigandChangeHandler(pocketUiCollection) {
	return function () {
		updateJmolLigand(pocketUiCollection);
	};
}


function generateSelectDistanceChangeHandler(pocketUiCollection) {
	return function () {
		updateJmolLigand(pocketUiCollection);
	};
}


function generateSelectChainChangeHandler(pocketUiCollection) {
	return function () {
		var suffixJmol = pocketUiCollection.jmolSuffix;
		var selectChainId = "#" + pocketUiCollection.selectChainId;
		
		var chainId = $(selectChainId + " option:selected").val();
		
		var script = "";
		script += "select all; wireframe only; zoom 0;";
		script += "select all and chain=" + chainId + ";";
		script += "define myChain selected;";
		jmolScriptWait(script, suffixJmol);
		
		updateSelectUi(pocketUiCollection);
		updateSequenceUi(pocketUiCollection);
	};
}


function updateJmolLigand(pocketUiCollection) {
	var suffixJmol = pocketUiCollection.jmolSuffix;
	var selectChainId = "#" + pocketUiCollection.selectChainId;
	var selectLigandId = "#" + pocketUiCollection.selectLigandId;
	var selectDistanceId = "#" + pocketUiCollection.selectDistanceId;
	var sequenceContainerId = "#" + pocketUiCollection.sequenceContainerId;
	
	var chainId = $(selectChainId + " option:selected").val();
	var ligandResidueId = $(selectLigandId + " option:selected").val();
	var distance = $(selectDistanceId + " option:selected").val();
	
	/* Locate binding site in Jmol. */
	var script = "select all; wireframe only;";
	// If no chain specified:
	if (chainId == "Z") {
		script += "select all; wireframe only; zoom 0;";
		script += "select " + ligandResidueId + ";";
		script += "define myLigand selected;";
		script += "select GROUPS within(" + distance + "," + ligandResidueId + ") and not ligand;";
		script += "define mySite selected;";
	}
	// If chain specified:
	else {
		script += "select all and chain=" + chainId + ";";
		script += "wireframe only; zoom 0;";
		script += "select " + ligandResidueId + " and chain=" + chainId + ";";
		script += "define myLigand selected;";
		script += "select GROUPS within(" + distance + "," + ligandResidueId + ") and not ligand and chain=" + chainId + ";";
		script += "define mySite selected;";
	}
	jmolScriptWait(script, suffixJmol);
	
	/* Get site residue IDs from Jmol. */
	jmolScriptWait("siteResidueIds={*.ca and mySite}.label('%[resno]');", suffixJmol);
	// residue IDs of selected binding site
	var siteResidueIds = jmolEvaluate("siteResidueIds", suffixJmol);
	siteResidueIds = siteResidueIds.replace(/\n/g, ", ");
	siteResidueIds = "([" + siteResidueIds + "])";
	siteResidueIds = eval(siteResidueIds);
	
	/* Select residue. */
	$(sequenceContainerId + " .residue").removeClass("selected");
	for (var i in siteResidueIds) {
		var residueId = siteResidueIds[i];
		$(sequenceContainerId + " .residue[residue-id=" + residueId + "]").addClass("selected");
	}
	
	/* Render binding site in Jmol. */
	script = "";
	script += "select myLigand; wireframe 0.3; spacefill 0.5;";
	script += "center myLigand; zoomto in;";
	script += "select mySite; wireframe 0.3;";
	jmolScript(script, suffixJmol);
}


function updateSelectUi(pocketUiCollection) {
	var suffixJmol = pocketUiCollection.jmolSuffix;
	var selectChainId = "#" + pocketUiCollection.selectChainId;
	var selectLigandId = "#" + pocketUiCollection.selectLigandId;
	
	var chainId = $(selectChainId + " option:selected").val();
	
	var script ="";
	script += "select all and chain=" + chainId + ";";
	script += "define myChain selected;";
	script += "ligandResidueIds={hetero and !(water) and myChain}.label('%[resno]');";
	jmolScriptWait(script, suffixJmol);
	
	// ligand residue ID array
	var ligandResidueIds = jmolEvaluate("ligandResidueIds", suffixJmol);
	ligandResidueIds = ligandResidueIds.replace(/\n/g, ", ");
	ligandResidueIds = "([" + ligandResidueIds + "])";
	ligandResidueIds = eval(ligandResidueIds);
	ligandResidueIds = filterDuplicate(ligandResidueIds);
	
	/* Update ligand selector. */
	jLigandSelector = $(selectLigandId);
	jLigandSelector.empty();
	$("<option/>").val(-1).text("No ligand chosen.").appendTo(jLigandSelector);
	for (var i in ligandResidueIds) {
		var residueId = ligandResidueIds[i];
		$("<option/>").val(residueId).text("Ligand: " + residueId).appendTo(jLigandSelector);
	}
}


function updateSequenceUi(pocketUiCollection) {
	var suffixJmol = pocketUiCollection.jmolSuffix;
	var selectChainId = "#" + pocketUiCollection.selectChainId;
	var sequenceContainerId = "#" + pocketUiCollection.sequenceContainerId;
	
	var chainId = $(selectChainId + " option:selected").val();
	
	var script = "";
	script += "select all and chain=" + chainId + ";";
	script += "define myChain selected;";
	script += "proteinResidueMap={protein and *.ca and myChain}.label('\"%[resno]\": \"%[group]\"');";
	script += "proteinResidueIds={protein and *.ca and myChain}.label('%[resno]');";
	jmolScriptWait(script, suffixJmol);
	
	// protein residue map: residue ID as key, residue name as value
	var proteinResidueMap = jmolEvaluate("proteinResidueMap", suffixJmol);
	proteinResidueMap = proteinResidueMap.replace(/\n/g,", ");
	proteinResidueMap = "({" + proteinResidueMap + "})";
	proteinResidueMap = eval(proteinResidueMap);
	
	// protein residue ID array
	var proteinResidueIds = jmolEvaluate("proteinResidueIds", suffixJmol);
	proteinResidueIds = proteinResidueIds.replace(/\n/g, ", ");
	proteinResidueIds = "([" + proteinResidueIds + "])";
	proteinResidueIds = eval(proteinResidueIds);
	proteinResidueIds.sort(function (a, b) {
		return a - b;
	});
	
	/* Update sequence container. */
	jSequenceContainer = $(sequenceContainerId);
	jSequenceContainer.empty();
	for (var i in proteinResidueIds) {
		var residueIdString = proteinResidueIds[i].toString();
		residueName = proteinResidueMap[residueIdString];
		if (residueName) {
			var jResidueId = $("<p/>").text(residueIdString + ": ").addClass("residue-id");
			var jResidueName = $("<p/>").text(residueName).addClass("residue-name");
			$("<a/>")
				.addClass("residue")
				.attr("residue-id", residueIdString)
				.append(jResidueId)
				.append(jResidueName)
				.appendTo(jSequenceContainer);
		}
	}
	
	/* Residue click handler. */
	$(sequenceContainerId + " .residue").click(function () {
		if ($(this).hasClass("selected")) {
			$(this).removeClass("selected");
			var residueId = $(this).attr("residue-id");
			jmolScript("select " + residueId + " and myChain; wireframe 1;", suffixJmol);
		}
		else {
			$(this).addClass("selected");
			var residueId = $(this).attr("residue-id");
			jmolScript("select " + residueId + " and myChain; wireframe 0.3;", suffixJmol);
		}
	});
}


function bindResidueSelectorInput(pocketUiCollection) {
	var inputResidueSelectorId = "#" + pocketUiCollection.inputResidueSelectorId;
	var sequenceContainerId = "#" + pocketUiCollection.sequenceContainerId;

	residueSelector = [];
	$(sequenceContainerId + " .residue.selected").each(function () {
		residueSelector.push($(this).attr("residue-id"));
	});
	residueSelector = residueSelector.join("+");
	$(inputResidueSelectorId).val(residueSelector);
}


function generateResidueSelectorInputBindingFunction(refPocketUiCollection, fitPocketUiCollection) {
	return function () {
		bindResidueSelectorInput(refPocketUiCollection);
		bindResidueSelectorInput(fitPocketUiCollection);
	};
}


$(document).ready(function() {
	var jmolSuffixRef = "ref";
	var jmolPanelIdRef = "jmol-panel-ref";
	var jmolContainerIdRef = "jmol-ref";
	var buttonLoadIdRef = "button-load-ref";
	var buttonAnalyzeIdRef = "button-analyze-ref";
	var inputPdbCodeIdRef = "input-pdb-code-ref";
	var inputResidueSelectorIdRef = "residue-selector-ref";
	var selectChainIdRef = "select-chain-ref";
	var selectLigandIdRef = "select-ligand-ref";
	var selectDistanceIdRef = "select-distance-ref";
	var sequencePanelIdRef = "sequence-panel-ref";
	var sequenceContainerIdRef = "container-sequence-ref";
	var refPocketUiCollection = new PocketUiCollection(
		jmolSuffixRef,
		jmolPanelIdRef,
		jmolContainerIdRef,
		buttonLoadIdRef,
		buttonAnalyzeIdRef,
		inputPdbCodeIdRef,
		inputResidueSelectorIdRef,
		selectChainIdRef,
		selectLigandIdRef,
		selectDistanceIdRef,
		sequencePanelIdRef,
		sequenceContainerIdRef
		);
	
	$("#" + refPocketUiCollection.buttonLoadId).click(generateJmolLoadButtonClickHandler(refPocketUiCollection));
	$("#" + refPocketUiCollection.buttonAnalyzeId).click(generateButtonAnalyzeClickHandler(refPocketUiCollection));
	$("#" + refPocketUiCollection.selectChainId).change(generateSelectChainChangeHandler(refPocketUiCollection));
	$("#" + refPocketUiCollection.selectLigandId).change(generateSelectLigandChangeHandler(refPocketUiCollection));
	$("#" + refPocketUiCollection.selectDistanceId).change(generateSelectDistanceChangeHandler(refPocketUiCollection));
	
	var jmolSuffixFit = "fit";
	var jmolPanelIdFit = "jmol-panel-fit";
	var jmolContainerIdFit = "jmol-fit";
	var buttonLoadIdFit = "button-load-fit";
	var buttonAnalyzeIdFit = "button-analyze-fit";
	var inputPdbCodeIdFit = "input-pdb-code-fit";
	var inputResidueSelectorIdFit = "residue-selector-fit";
	var selectChainIdFit = "select-chain-fit";
	var selectLigandIdFit = "select-ligand-fit";
	var selectDistanceIdFit = "select-distance-fit";
	var sequencePanelIdFit = "sequence-panel-fit";
	var sequenceContainerIdFit = "container-sequence-fit";
	var fitPocketUiCollection = new PocketUiCollection(
		jmolSuffixFit,
		jmolPanelIdFit,
		jmolContainerIdFit,
		buttonLoadIdFit,
		buttonAnalyzeIdFit,
		inputPdbCodeIdFit,
		inputResidueSelectorIdFit,
		selectChainIdFit,
		selectLigandIdFit,
		selectDistanceIdFit,
		sequencePanelIdFit,
		sequenceContainerIdFit
		);
	
	$("#" + fitPocketUiCollection.buttonLoadId).click(generateJmolLoadButtonClickHandler(fitPocketUiCollection));
	$("#" + fitPocketUiCollection.buttonAnalyzeId).click(generateButtonAnalyzeClickHandler(fitPocketUiCollection));
	$("#" + fitPocketUiCollection.selectChainId).change(generateSelectChainChangeHandler(fitPocketUiCollection));
	$("#" + fitPocketUiCollection.selectLigandId).change(generateSelectLigandChangeHandler(fitPocketUiCollection));
	$("#" + fitPocketUiCollection.selectDistanceId).change(generateSelectDistanceChangeHandler(fitPocketUiCollection));
	
	$("#submit").click(generateResidueSelectorInputBindingFunction(
		refPocketUiCollection,
		fitPocketUiCollection
		));
	
	$("input:text").keypress(function (event) {
		if (event.keyCode == 13) {
			return false;
		}
	});
	
	
	/* Pin button for jmol panel. */
	$("#" + refPocketUiCollection.jmolPanelId + " .button-pin").click(function () {
		$(this).toggleClass("selected");
		$("#" + refPocketUiCollection.jmolPanelId).toggleClass("fixed");
	});
	$("#" + fitPocketUiCollection.jmolPanelId + " .button-pin").click(function () {
		$(this).toggleClass("selected");
		$("#" + fitPocketUiCollection.jmolPanelId).toggleClass("fixed");
	});
	
	/* Hide initial empty panels. */
	$("#" + refPocketUiCollection.jmolPanelId).hide();
	$("#" + fitPocketUiCollection.jmolPanelId).hide();
	$("#" + refPocketUiCollection.sequencePanelId).hide();
	$("#" + fitPocketUiCollection.sequencePanelId).hide();
});