<style>
  #chat {
    background-color: white;
    height: 500px;
    overflow-y: scroll
  }
</style>

<script>
  // scroll the chat div to the bottom
  function scrollChat() {
    var chatDiv = document.getElementById('chat');
    chatDiv.scrollTop = chatDiv.scrollHeight;
  };
</script>
