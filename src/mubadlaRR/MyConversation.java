package mubadlaRR;

import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;


public class MyConversation {

	public static void main(String[] args) {
		ConversationService service = new ConversationService("2017-02-03");
		service.setUsernameAndPassword("b9505d07-6806-48f9-93bc-2508bef58975", "a4yC3hOSgoEF");


		MessageRequest newMessage = new MessageRequest.Builder()
		  .inputText("hi there")
		  // Replace with the context obtained from the initial request
		  //.context(...)
		  .build();

		String workspaceId = "78cd0238-d606-497c-9940-92f8620c9e5f";

		MessageResponse response = service
		  .message(workspaceId, newMessage)
		  .execute();

		System.out.println(response);

	}

}
