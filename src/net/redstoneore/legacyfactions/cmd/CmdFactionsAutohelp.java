package net.redstoneore.legacyfactions.cmd;

import java.util.ArrayList;

import net.redstoneore.legacyfactions.Factions;
import net.redstoneore.legacyfactions.config.CommandAliases;
import net.redstoneore.legacyfactions.entity.FPlayerColl;
import net.redstoneore.legacyfactions.event.EventFactionsCommandExecute;
import net.redstoneore.legacyfactions.lang.Lang;
import net.redstoneore.legacyfactions.util.TextUtil;

public class CmdFactionsAutohelp extends FCommandBase<Factions> {

	// -------------------------------------------------- //
	// INSTANCE
	// -------------------------------------------------- //
	
	private static CmdFactionsAutohelp instance = new CmdFactionsAutohelp();
	public static CmdFactionsAutohelp get() { return instance; }

	// -------------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------------- //

	private CmdFactionsAutohelp() {
		this.aliases.addAll(CommandAliases.cmdAliasesAutohelp);

		this.setHelpShort("");

		this.optionalArgs.put("page", "1");
	}

	// -------------------------------------------------- //
	// METHODS
	// -------------------------------------------------- //
	
	@Override
	public final void prePerform() {
		if (this.senderIsConsole) {
			if (EventFactionsCommandExecute.create(null, this).call().isCancelled()) {
				return;
			}
		} else {
			if (EventFactionsCommandExecute.create(FPlayerColl.get(this.sender), this).call().isCancelled()) {
				return;
			}
		}
		
		this.perform();
	}
	
	@Override
	public void perform() {
		if (this.commandChain.isEmpty()) return;
		
		FCommandBase<?> pcmd = this.commandChain.get(this.commandChain.size() - 1);

		ArrayList<String> lines = new ArrayList<>();

		lines.addAll(pcmd.helpLong);

		for (FCommandBase<?> scmd : pcmd.getSubcommands()) {
			if (scmd.visibility == CommandVisibility.VISIBLE || (scmd.visibility == CommandVisibility.SECRET && scmd.validSenderPermissions(sender, false))) {
				lines.add(scmd.getUseageTemplate(this.commandChain, true));
			}
		}

		sendMessage(TextUtil.get().getPage(lines, this.argAsInt(0, 1), Lang.COMMAND_AUTOHELP_HELPFOR.toString() + pcmd.aliases.get(0) + "\""));
	}

	@Override
	public String getUsageTranslation() {
		return Lang.COMMAND_HELP_DESCRIPTION.toString();
	}
	
}
