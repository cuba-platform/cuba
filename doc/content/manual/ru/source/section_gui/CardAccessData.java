public class CardAccessData extends AbstractWfAccessData {

  private Card card
  private Boolean saveEnabled

  def CardAccessData(Map params) {
    super(params);
    card = params['param$item']
  }

  boolean getNotStarted() {
    if (PersistenceHelper.isNew(card))
      return true
    else
      return card.jbpmProcessId == null
  }

  boolean getSaveEnabled() {
    if (saveEnabled == null) {
      if (card.jbpmProcessId == null)
        saveEnabled = true
      else {
        LoadContext ctx = new LoadContext(CardRole.class)
        Query q = ctx.setQueryString('select cr.id from wf$CardRole cr where cr.card.id = :card and cr.user.id = :user')
        q.addParameter('card', card.getId())
        q.addParameter('user', UserSessionClient.currentOrSubstitutedUserId())
        List list = ServiceLocator.getDataService().loadList(ctx)
        saveEnabled = !list.isEmpty()
      }
    }
    return saveEnabled
  }
}