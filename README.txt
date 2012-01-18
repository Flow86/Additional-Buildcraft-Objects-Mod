
Additional Buildcraft Objects:

---------------------------------------------------------------------------------

Round Robin Transport Pipe
  will change output per item(stack)

Recipe:
  StoneTransportPipe Gravel

---------------------------------------------------------------------------------

Compactor Pipe
  will merge same items to larger itemstacks
  has to be powered with redstone
  ignores itemstacks larger than 16 items (only compacts smaller ones)

Recipe:
  StoneTransportPipe Piston

---------------------------------------------------------------------------------

Insertion Transport Pipe
  Insertion Pipes given the choice between putting a item in the pipe in to an 
  inventory (A chest or a furnace) and passing it along to another pipe, the
  Insertion Pipe will always try to add the item to the inventory. 

  This pipe is very useful for feedback loops as it forces the item to go in 
  to the last machine if it can.

Recipe:
  StoneTransportPipe Redstone


---------------------------------------------------------------------------------

Extraction Transport Pipe
  Extraction Pipe is the opposite of the Insertion Pipe. If the pipe can choose 
  between an inventory and another pipe the pipe will always go with the next 
  pipe. Also this pipe behaves the same as a wood pipe where it will pull items
  out of an inventory if the pipe has an active redstone engine applied to it. 

  This pipe is useful for tight spaces where you do not want a pipe to 
  accidentally fill a chest or machine. 

Recipe:
  StoneTransportPipe Planks

---------------------------------------------------------------------------------

Bounce Transport Pipe
  This pipe will cause whatever enters the pipe to come back out the way it came 
  unless the pipe is powered by redstone, then it behaves as a normal pipe.

Recipe:
  StoneTransportPipe Cobblestone

---------------------------------------------------------------------------------

Crossover Transport Pipe
  This pipe will direct material entering it to the pipe 
  (or something accepting material, like chests) directly across from it. 
  If no such pipe exists, a random direction is chosen.

Recipe:
  StoneTransportPipe IronTransportPipe

---------------------------------------------------------------------------------

Valve Pipe
  acts like a wooden pipe (without need of wooden engine)
  also acts like a, yes ..., valve ;-)


Recipe:
  WoodenWaterproofPipe Lever WoodenWaterproofPipe 

---------------------------------------------------------------------------------

Golden Iron Waterproof Pipe
  acts like a iron pipe with speed of a golden pipe

Recipe:
  GoldenWaterproofPipe IronWaterproofPipe 

---------------------------------------------------------------------------------

Balance Pipe
  balances liquid of connected tanks when powered 
  with redstone

  Thanks to Oxygene13 for his idea :-)

Recipe:
  WoodenWaterproofPipe IronORGate WoodenWaterproofPipe

---------------------------------------------------------------------------------

Diamond Waterproof Pipe
  acts like a diamond pipe but for liquids.
  every filter item not being a bucket or liquid gets ignored.

Recipe:
  DiamondTransportPipe PipeWaterproof

---------------------------------------------------------------------------------

Power Switch Pipe
  acts as lever for power pipes

Recipe:
  GoldenConductivePipe Lever

---------------------------------------------------------------------------------

Safe-Engine Trigger
  acts like the blue & green state trigger, so it will be active as long the 
  engines are "blue" or "green"

  i.e power them up via an AND gate and an external wire signal, but have an 
  "emergency stop"

---------------------------------------------------------------------------------

Changelog:

0.9:
 - removed engine control pipe (unnecessary now, buildcraft has a trigger api now)
 - mc 1.1 compatible
 - bc 3.1.2/2.2.12 compatible

0.8.2:
 - added diamond liquid pipe


0.8.1:
 - added engine control pipe

0.8:
 - added pipes from old "ExtraBuildcraftPipes"-Mod by leftler/blakmajik
   ° Insertion Transport Pipe
   ° Extraction Transport Pipe
   ° Bounce Transport Pipe
   ° Crossover Transport Pipe
   
   see: http://www.minecraftforum.net/topic/474348-173-2012-extrabuildcraftpipes/

   oh and YES - they gave me the permission (before someones shouting about copyright...)

0.7:
 - bc 3.0.4 compat
 - removed obsolete Redstone Power Converter
 - removed "Invalid Pipe" (buildcraft now reacts correctly on unknown pipe ids)

0.6f:
 - fix missing class file (damn packaging script ...)

0.6e:
 - fix valve crash 2 (sorry again!!)

0.6d:
 - fix valve crash (sorry!!)

0.6c:
 - bc 3.0.3 compat
 - mc 1.0.0 compat

0.6b:
 - bc 3.0.2 compat
 - fixed power switch pipe

0.6:
 - bc 3.0.1 compat
 - removed flowmeter (obsolete)


0.5b:
 - fixed bc > 2.2.2 compat

0.5:
 - added Redstone Power Converter
 - added Compactor Pipe

0.4:
 - added Balance Pipe

0.3:
 - added Flow Meter Liquids Pipe
 - changed recipes

0.2:
 - fixed Valve Liquids Pipe
 - added Power Switch Pipe
 - added Golden Iron  Liquids Pipe

0.1:
 - added Round Robin Transport Pipe
 - added Valve Liquids Pipe

---------------------------------------------------------------------------------
