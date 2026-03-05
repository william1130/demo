/**
 * Folding object template.
 */
function FoldingObj( id , collapsed , description , expendedImage , collapsedImage )
{
	this.foldingBlock = new FoldingBlock( {oldId: id, collapsed: collapsed, description: description, expendedImage: expendedImage, collapsedImage: collapsedImage} );
}

function autoFolding( foldingObj, collapsed )
{
	foldingObj.foldingBlock.autoFolding( collapsed );
}

