package ${packageValue};

<#list imports as importName>
import ${importName};
</#list>
public class BaseSv<M extends BaseMapper<T, I, E>, T extends MybatisEntity, I, E> {
    protected M mapper;
    protected Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public void setMyRepository(M mapper) {
        this.mapper = mapper;
    }

    public List<T> list() {
        return this.mapper.selectByExample(null);
    }

    protected List<T> list(E exmp) {
        return this.mapper.selectByExample(exmp);
    }

    public T get(I id) {
        return this.mapper.selectByPrimaryKey(id);
    }

    public void del(I id) {
        this.mapper.deleteByPrimaryKey(id);
    }
}