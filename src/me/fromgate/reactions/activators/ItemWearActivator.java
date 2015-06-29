package me.fromgate.reactions.activators;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.event.ItemWearEvent;
import me.fromgate.reactions.util.Variables;
import me.fromgate.reactions.util.item.ItemUtil;
import me.fromgate.reactions.util.item.VirtualItem;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;

public class ItemWearActivator extends Activator {
	private String item;

	public ItemWearActivator(String name, String group, YamlConfiguration cfg) {
		super(name, group, cfg);
	}

	public ItemWearActivator(String name, String item){
		super (name,"activators");
		this.item = item;
	}


	@Override
	public boolean activate(Event event) {
		if (item.isEmpty()||(ItemUtil.parseItemStack(item)==null)) {
			ReActions.util.logOnce(this.name+"activatorwearempty", "Failed to parse item of activator "+this.name);
			return false;
		}
		if (event instanceof ItemWearEvent){
			ItemWearEvent iw  = (ItemWearEvent) event;
			if (iw.isItemWeared(this.item)) {
				VirtualItem vi = ItemUtil.itemFromItemStack(iw.getFoundedItem(this.item));
				if (vi!=null&&vi.getType()!=Material.AIR){
					Variables.setTempVar("item", vi.toString());
					Variables.setTempVar("item-str", vi.toDisplayString());
				}
				return Actions.executeActivator(iw.getPlayer(), this);
			}
		}
		return false;
	}

	@Override
	public boolean isLocatedAt(Location loc) {
		return false;
	}

	@Override
	public void save(String root, YamlConfiguration cfg) {
		cfg.set(root+".item",this.item);
	}

	@Override
	public void load(String root, YamlConfiguration cfg) {
		this.item=cfg.getString(root+".item");
	}

	@Override
	public ActivatorType getType() {
		return ActivatorType.ITEM_WEAR;
	}

	public String getItemStr(){
		return this.item;
	}
	
	 @Override
	 public String toString(){
		 StringBuilder sb = new StringBuilder (name).append(" [").append(getType()).append("]");
		 if (!getFlags().isEmpty()) sb.append(" F:").append(getFlags().size());
		 if (!getActions().isEmpty()) sb.append(" A:").append(getActions().size());
		 if (!getReactions().isEmpty()) sb.append(" R:").append(getReactions().size());
		 sb.append(" (").append(this.item).append(")");
		 return sb.toString();
	 }
	
}

