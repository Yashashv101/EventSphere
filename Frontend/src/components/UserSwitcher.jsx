import {useUser} from "../context/UserContext.jsx";

function UserSwitcher(props){
    const {currentRole,switchRole,roles}=useUser();
    return(
        <div className="flex items-center gap-4 px-4 py-3 bg-slate-50 rounded-lg mb-4 mx-8 mt-4">
            <span className="font-medium text-slate-500 text-sm">Switch User:</span>
            <div className="flex gap-2">
                {Object.entries(roles).map(([roleKey,roleData])=>(
                    <button>
                        key={roleKey}
                        className={`px-4 py-2 border-2 rounded-md font-semibold text-sm cursor-pointer transition-all duration-200 hover:opacity-80 hover:-translate-y-px ${currentRole === roleKey ? 'shadow-md' : ''
                            }`}
                        style={{
                            backgroundColor:currentRole===roleKey ? roleData.color : 'transparent',
                            color:currentRole===roleKey ? 'white' : roleData.color,
                            borderColor:roleData.color,
                    }}
                    onClick={()=>switchRole(roleKey)}
                        {roleData.label}
                    </button>
                ))}
            </div>
        </div>
    )
}

export default UserSwitcher;