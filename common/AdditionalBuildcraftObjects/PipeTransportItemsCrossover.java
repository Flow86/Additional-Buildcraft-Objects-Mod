package AdditionalBuildcraftObjects;

import java.util.LinkedList;

import buildcraft.api.EntityPassiveItem;
import buildcraft.api.Orientations;
import buildcraft.api.Position;
import buildcraft.transport.PipeTransportItems;

/**
 * This pipe will always prefer to use the opposite direction,
 * so items will go "straight through"
 * 
 * @author blakmajik
 *         ported to BC > 2.2 by Flow86
 */
public class PipeTransportItemsCrossover extends PipeTransportItems {
	@Override
	public LinkedList<Orientations> getPossibleMovements(Position pos, EntityPassiveItem item) {
		LinkedList<Orientations> list = new LinkedList<Orientations>();
		
		Position newPos = new Position(pos);
		newPos.moveForwards(1.0);
		if (canReceivePipeObjects(newPos, item))
			list.add(newPos.orientation);
		else
			list = super.getPossibleMovements(pos, item);
			
		return list;
	}
}
