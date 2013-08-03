package pl.edu.agh.ki.mmorts.server.modules.basic.buildings;

import java.util.List;

import pl.edu.agh.ki.mmorts.common.message.Message;
import pl.edu.agh.ki.mmorts.server.data.CustomPersistor;
import pl.edu.agh.ki.mmorts.server.modules.Context;
import pl.edu.agh.ki.mmorts.server.modules.ModuleBase;
import pl.edu.agh.ki.mmorts.server.modules.annotations.MessageMapping;
import protocol.buildingsModule.BuildingInstance;
import protocol.buildingsModule.BuildingMessage;
import protocol.buildingsModule.Requests;
import protocol.mapModule.MapModuleData;
import protocol.mapModule.helpers.DetailedMessage;
import protocol.mapModule.helpers.FieldContent;
import protocol.mapModule.helpers.ImmutableBoard;

import com.google.inject.Inject;

/*
 * tak
 no niech da się zbudować budynek i niech tam ma jakiś Object do wyświetlenia w każdym budynku
 niech to bedzie string;)
 i niech tam będzie kilka budynków i niech on sobie zapisuje to w bazie
 i niech się komunikuje z modułem mapy żeby sprawdzić czy gdzieśtam jest miejsce
 i niech "zajmuje" tę mapę
 */

/**
 * "can-build", BuildingMessage - queries the module whether the building can be
 * built. Returns "yes-can-build" to the local message source if the building
 * can be built, "no-can-build" otherwise.
 * 
 * "can-demolish", BuildingMessage - as "can-build"
 * 
 * "build", BuildingMessage - creates new building. Sends "build-success" to the
 * client, with no attached data.
 * 
 * "get-buildings", BuildingMessage - requests the building list, which is sent
 * with the "building-list"
 * 
 * @author los
 */
public class BuildingsModule extends ModuleBase {
    
    private static final Object PLACEHOLDER = new Object();
    
    private static final String BUILD = "processing-can-build";
    
    private static final String DEMOLISH = "processing-can-demolish";

    @Inject(optional = true)
    @pl.edu.agh.ki.mmorts.server.core.annotations.CustomPersistor
    private CustomPersistor persistor;

    @Override
    public void init() {
        logger().debug("Initialized");
    }

    @MessageMapping(Requests.CAN_BUILD)
    public void canBuild(Message message, Context ctx) {
        BuildingMessage msg = message.get(BuildingMessage.class);
        BuildingInstance building = msg.getBuilding();
        ctx.put("building", building);
        ctx.put("message", message);
//        ctx.put(BUILD, PLACEHOLDER);
        send("map_mod", Requests.FULL_INTERNAL);
    }

    @MessageMapping(Requests.FULL_INTERNAL)
    public void receiveMap(Message message, Context ctx) {
        ImmutableBoard board = message.get(MapModuleData.class).getBoard();
        BuildingInstance building = ctx.get("building", BuildingInstance.class);
        int width = building.getData().getWidth();
        int height = building.getData().getHeight();
        int row = building.getRow();
        int col = building.getColumn();
        String response = checkRect(row, col, width, height, board) ? Requests.YES_CAN_BUILD
                : Requests.NO_CAN_BUILD;
        outputResponse(ctx.get("message", Message.class), response);
    }

    private boolean checkRect(int row, int col, int width, int height,
            ImmutableBoard board) {
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                if (board.getAt(row + j, col + i) != FieldContent.G) {
                    return false;
                }
            }
        }
        return true;
    }

    @MessageMapping(Requests.BUILD)
    public void build(Message message, Context ctx) {
        BuildingMessage msg = message.get(BuildingMessage.class);
        BuildingInstance building = msg.getBuilding();
        String player = msg.getPlayer();
        int row = building.getRow();
        int col = building.getColumn();
        DetailedMessage details = new DetailedMessage(player, row, col);
        send("map_mod", Requests.PUT_ON, details);
        @SuppressWarnings("unchecked")
        List<BuildingInstance> list = (List<BuildingInstance>) persistor
                .receiveBinding(name(), player, List.class);
        list.add(building);
        persistor.updateBinding(name(), player, list);
        outputResponse(message, Requests.BUILD_SUCCESS);
    }
    
    @MessageMapping(Requests.GET_BUILDINGS)
    public void getBuildings(Message message, Context ctx) {
        BuildingMessage msg = message.get(BuildingMessage.class);
        String player = msg.getPlayer();
        @SuppressWarnings("unchecked")
        List<BuildingInstance> list = (List<BuildingInstance>) persistor
                .receiveBinding(name(), player, List.class);
        outputResponse(message, Requests.GET_BUILDINGS, list);
    }
    
    @MessageMapping(Requests.DEMOLISH)
    public void demolish(Message message, Context ctx) {
        BuildingMessage msg = message.get(BuildingMessage.class);
        BuildingInstance building = msg.getBuilding();
        String player = msg.getPlayer();
        int row = building.getRow();
        int col = building.getColumn();
        DetailedMessage details = new DetailedMessage(player, row, col);
        send("map_mod", Requests.REL_AT, details);
        @SuppressWarnings("unchecked")
        List<BuildingInstance> list = (List<BuildingInstance>) persistor
                .receiveBinding(name(), player, List.class);
        list.remove(building);
        persistor.updateBinding(name(), player, list);
        outputResponse(message, Requests.DEMOLISH_SUCCESS);
    }

}
